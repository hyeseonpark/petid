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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.databinding.ActivityMyInfoBinding
import com.android.petid.databinding.FragmentMyInfoDetailBinding
import com.android.petid.databinding.FragmentMyInfoUpdateBinding
import com.android.petid.ui.state.CommonApiState
import com.android.petid.viewmodel.my.MyInfoViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 마이페이지 메인 > 내 정보
 */
@AndroidEntryPoint
class MyInfoDetailFragment : Fragment() {
    private lateinit var binding: FragmentMyInfoDetailBinding
    private val viewModel: MyInfoViewModel by activityViewModels()

    private val TAG = "MyInfoActivity"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyInfoDetailBinding.inflate(inflater)
        initComponent()
        viewModel.getMemberInfo()
        observeGetMemberInfoState()
        return binding.root
    }


    private fun initComponent() {
        with(binding) {
            buttonBack.setOnClickListener {
                activity?.finish()
            }
            textViewUpdate.setOnClickListener{
                findNavController().navigate(R.id.action_myInfoDetailFragment_to_myInfoUpdateFragment)
            }
            requestCameraPermission()

            // Glide.with(this).load(R.drawable.myimage).circleCrop().into(binding.imageViewProfile);
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
                        if(Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver,
                                currentImageUri
                            )
                            binding.imageViewProfile.setImageBitmap(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(requireContext().contentResolver, currentImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageViewProfile.setImageBitmap(bitmap)
                        }
                    }


                }catch(e:Exception) {
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
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
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

                            image?.takeIf { it.isNotBlank() }?.let {
                                Glide.with(requireContext()).load(it).into(binding.imageViewProfile)
                            }

                            binding.textViewName.text = name
                            // binding.textViewBirth.text =
                            binding.textViewPhoneNumber.text = phone
                            binding.textViewAddress.text =
                                listOfNotNull(address, addressDetails)
                                .filter { it.isNotBlank() }
                                .joinToString("\n")
                        }
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                    }
                }
            }
        }
    }
}