package com.petid.petid.ui.view.my

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.petid.petid.R
import com.petid.petid.common.Constants.CHIP_TYPE
import com.petid.petid.databinding.FragmentPetInfoDetailBinding
import com.petid.petid.ui.component.CustomDialogCommon
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.util.TAG
import com.petid.petid.util.showErrorMessage
import com.petid.petid.viewmodel.my.PetInfoViewModel
import com.bumptech.glide.Glide
import com.petid.petid.util.throttleFirst
import com.petid.petid.util.toFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

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
        observeGetPetInfoState()
        observeGetPetImageState()
        observeUpdatePetPhotoState()

        initComponent()

        viewModel.getPetDetails()
    }

    private fun initComponent() {
        with(binding) {
            imageViewProfile
                .clicks()
                .throttleFirst()
                .onEach {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        with(Intent()) {
                            action = Intent.ACTION_PICK
                            type = MediaStore.Images.Media.CONTENT_TYPE
                            actionPick.launch(this)
                        }
                    } else {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            // 미등록 상태, dialog 보여주기
            textViewPetidStatusNull
                .clicks()
                .throttleFirst()
                .onEach {
                    CustomDialogCommon(
                        title = getString(R.string.pet_info_dialog_chip_na_desc),
                        boldTitle = getString(R.string.pet_info_dialog_chip_na_bold_title),
                        isSingleButton = true,
                        singleButtonText = getString(R.string.pet_info_dialog_chip_na_button)
                    ).show(childFragmentManager, null)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    /**
     * ( < TIRAMISU )
     */
    private val actionPick = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                processForUploadFile(uri)
            }
        }else{
            showErrorMessage("actionPick null")
        }
    }

    /**
     * photo picker ( >= TIRAMISU )
     */
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { result ->
        result?.let { uri ->
            processForUploadFile(uri)
        } ?: showErrorMessage("pickMedia null")
    }

    /**
     * uri -> file 변환 및 uploadFile 진행
     */
    private fun processForUploadFile(uri: Uri) {
        // s3 bucket 에 파일 업로드
        with(viewModel) {
            uploadFile(uri.toFile(requireActivity())!!, petImageFileName!!)
        }
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

    /**
     * viewModel.getPetImageUrl 결과값 view 반영
     */
    private fun observeUpdatePetPhotoState() {
        lifecycleScope.launch {
            viewModel.updatePetPhotoResult.collectLatest { result ->
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when (result) {
                    is CommonApiState.Success -> viewModel.getPetDetails()
                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    is CommonApiState.Loading -> showLoading()
                    is CommonApiState.Init -> {}
                }
            }
        }
    }
}