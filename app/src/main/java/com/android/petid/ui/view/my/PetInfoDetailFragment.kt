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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.common.Constants.CHIP_TYPE
import com.android.petid.databinding.FragmentPetInfoDetailBinding
import com.android.petid.ui.component.CustomDialogCommon
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.util.TAG
import com.android.petid.util.bitmapToFile
import com.android.petid.util.showErrorMessage
import com.android.petid.viewmodel.my.PetInfoViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PetInfoDetailFragment
    : BaseFragment<FragmentPetInfoDetailBinding>(FragmentPetInfoDetailBinding::inflate) {
    private val viewModel: PetInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetInfoDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
            onBackClick = { activity?.finish() },
            showUpdateButton = true,
            onUpdateClick = {
                findNavController().navigate(R.id.action_petInfoDetailFragment_to_petInfoUpdateFragment)
            },
            title = getString(R.string.pet_info_title),
        )
        initComponent()
        viewModel.getPetDetails()
        observeGetPetInfoState()
        observeGetPetImageState()
    }

    private fun initComponent() {
        with(binding) {
            requestCameraPermission()

            imageViewProfile.setOnClickListener{
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("image/*")
                filterActivityLauncher.launch(intent)
            }

            // 미등록 상태, dialog 보여주기
            textViewPetidStatusNull.setOnClickListener {
                CustomDialogCommon(
                    title = getString(R.string.pet_info_dialog_chip_na_desc),
                    boldTitle = getString(R.string.pet_info_dialog_chip_na_bold_title),
                    isSingleButton = true,
                    singleButtonText = getString(R.string.pet_info_dialog_chip_na_button)
                ).show(childFragmentManager, null)
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
                        with(viewModel) {
                            uploadFile(requireContext(), file, petImageFileName!!)
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
     * viewModel.getPetDetails 결과값 view 반영
     */
    private fun observeGetPetInfoState() {
        lifecycleScope.launch {
            viewModel.getPetDetailsResult.collectLatest { result ->
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when (result) {
                    is CommonApiState.Success -> {
                        with(result.data) {
                            binding.apply {
                                textViewName.text = petName
                                textViewBirth.text = petBirthDate
                                textViewGender.text = petSex
                                textViewType.text = appearance.breed
                                textViewWeight.text =
                                    String.format(getString(R.string.to_kg), appearance.weight)
                                textViewFeature.text =
                                    listOf(appearance.hairColor, appearance.hairLength).joinToString(", ")

                                when(chipType) {
                                    CHIP_TYPE[2] -> {
                                        textViewPetidStatusNull.visibility = View.GONE
                                        textViewPetidStatusHasData.visibility = View.VISIBLE
                                    }
                                    else -> {
                                        textViewPetidStatusNull.visibility = View.VISIBLE
                                        textViewPetidStatusHasData.visibility = View.GONE
                                    }
                                }
                            }
                        }
                    }
                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    is CommonApiState.Loading -> showLoading()
                    is CommonApiState.Init -> {}
                }
            }
        }
    }


    /**
     * viewModel.getPetImageUrl 결과값 view 반영
     */
    private fun observeGetPetImageState() {
        lifecycleScope.launch {
            viewModel.getPetImageUrlResult.collectLatest { result ->
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when (result) {
                    is CommonApiState.Success -> {
                        R.color.d9.let {
                            Glide.with(requireContext())
                                .load(result.data)
                                .placeholder(it)
                                .error(it)
                                .into(binding.imageViewProfile)
                        }
                    }
                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    is CommonApiState.Loading -> showLoading()
                    is CommonApiState.Init -> {}
                }
            }
        }
    }
}