package com.android.petid.ui.view.my

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.databinding.FragmentMyInfoDetailBinding
import com.android.petid.ui.state.CommonApiState
import com.android.petid.util.bitmapToFile
import com.android.petid.util.getRandomFileName
import com.android.petid.viewmodel.my.MyInfoViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 마이페이지 메인 > 내 정보
 */
@AndroidEntryPoint
class MyInfoDetailFragment
    : BaseFragment<FragmentMyInfoDetailBinding>(FragmentMyInfoDetailBinding::inflate) {

    companion object{
        fun newInstance()= MyInfoDetailFragment()
    }

    private val viewModel: MyInfoViewModel by activityViewModels()

    private val TAG = "MyInfoDetailFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyInfoDetailBinding.inflate(inflater)
        initComponent()
        viewModel.getMemberInfo()
        observeGetMemberInfoState()
        observeGetMemberImage()
        observeUploadS3ResultState()
        observeUpdateMemberPhotoResultState()
        return binding.root
    }


    private fun initComponent() {
        with(binding) {
            buttonBack.setOnClickListener { findNavController().popBackStack() }
            textViewUpdate.setOnClickListener{
                findNavController().navigate(R.id.action_myInfoDetailFragment_to_myInfoUpdateFragment)
            }
            requestCameraPermission()

            imageViewProfile.setOnClickListener{
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("image/*")
                filterActivityLauncher.launch(intent)
            }
        }
    }

    /**
     * 갤러리 이미지 결과값 처리
     */
    private val filterActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK && it.data !=null) {
                val currentImageUri = it.data?.data
                try {
                    currentImageUri?.let {
                        val bitmap = if(Build.VERSION.SDK_INT < 28) {
                            MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver,
                                currentImageUri
                            )
                        } else {
                            val source = ImageDecoder.createSource(
                                requireContext().contentResolver, currentImageUri)
                            ImageDecoder.decodeBitmap(source)
                        }
                        // 파일 생성
                        val file = bitmapToFile(requireContext(), bitmap, "profile_image.jpg")

                        // s3 bucket 에 파일 업로드
                        // TODO random name -> 1-1.5 회원정보조회 api에 컬럼 추가 후 수정
                        with(viewModel) {
                            uploadFile(requireContext(), file, memberImageFileName!!)
                        }
                    }

                } catch(e:Exception) {
                    e.printStackTrace()
                }
            } else if(it.resultCode == RESULT_CANCELED){
                // Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            } else {
                Log.d("ActivityResult","something wrong")
            }
        }

    /**
     * camera permission
     */
    private fun requestCameraPermission() {
        val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Log.d(TAG, "camera permission: $it")
        }
        permissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    /**
     * viewModel.getMemberInfoResult 결과값 view 반영
     */
    private fun observeGetMemberInfoState() {
        lifecycleScope.launch {
            viewModel.getMemberInfoResult.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        with(result.data) {
                            binding.apply {
                                textViewName.text = name
                                textViewPhoneNumber.text = phone
                                textViewAddress.text =
                                    listOfNotNull(address, addressDetails)
                                        .filter { it.isNotBlank() }
                                        .joinToString("\n")
                            }
                        }
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                    }
                    is CommonApiState.Init -> {}
                }
            }
        }
    }

    /**
     * viewModel.getMemberImageResult 결과값 view 반영
     */
    private fun observeGetMemberImage() {
        lifecycleScope.launch {
            viewModel.getMemberImageResult.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        result.data.let {
                            Glide.with(requireContext()).load(it).into(binding.imageViewProfile)
                        }
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                    }
                    is CommonApiState.Init -> {}
                }
            }
        }
    }

    /**
     * viewModel.uploadFile 결과값 반영
     */
    private fun observeUploadS3ResultState() {
        lifecycleScope.launch {
            viewModel.uploadS3Result.collectLatest { result ->
                when {
                    result.isSuccess -> {
                        viewModel.updateMemberPhoto(viewModel.memberImageFileName!!)
                    }
                    result.isFailure -> {
                        val exception = result.exceptionOrNull()
                        Log.d(TAG, exception?.message.toString())
                    }
                }
            }
        }
    }

    /**
     * viewModel.uploadFile 결과값 반영
     */
    private fun observeUpdateMemberPhotoResultState() {
        lifecycleScope.launch {
            viewModel.updateMemberPhotoResult.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        viewModel.getMemberInfo()
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                    }
                    is CommonApiState.Init -> {}
                }
            }
        }
    }
}