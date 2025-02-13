package com.petid.petid.ui.view.my

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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.petid.petid.R
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.databinding.FragmentMyInfoDetailBinding
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.util.TAG
import com.petid.petid.util.showErrorMessage
import com.petid.petid.viewmodel.my.MyInfoViewModel
import com.bumptech.glide.Glide
import com.petid.petid.util.throttleFirst
import com.petid.petid.util.toCompressedByteArray
import com.petid.petid.util.toFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

/**
 * 마이페이지 메인 > 내 정보
 */
@AndroidEntryPoint
class MyInfoDetailFragment
    : BaseFragment<FragmentMyInfoDetailBinding>(FragmentMyInfoDetailBinding::inflate) {
    private val viewModel: MyInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyInfoDetailBinding.inflate(inflater)
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
                findNavController().navigate(R.id.action_myInfoDetailFragment_to_myInfoUpdateFragment)
            },
            title = getString(R.string.my_info_title),
        )
        observeGetMemberInfoState()
        observeGetMemberImage()
        observeUploadS3ResultState()
        observeUpdateMemberPhotoResultState()

        initComponent()

        viewModel.getMemberInfo()
    }

    private fun initComponent() {
        with(binding) {
            imageViewProfile
                .clicks()
                .throttleFirst()
                .onEach {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        with(Intent()) {
                            action = Intent.ACTION_GET_CONTENT
                            type = "image/*"
                            actionPick.launch(this)
                        }
                    } else {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
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
            uploadFile(uri.toCompressedByteArray(requireActivity())!!, memberImageFileName!!)
        }
    }

    /**
     * viewModel.getMemberInfoResult 결과값 view 반영
     */
    private fun observeGetMemberInfoState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getMemberInfoResult.collectLatest { result ->
                    if (result !is CommonApiState.Loading)
                        hideLoading()

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
                        is CommonApiState.Error -> showErrorMessage(result.message.toString())
                        is CommonApiState.Loading -> showLoading()
                        is CommonApiState.Init -> {}
                    }
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
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when (result) {
                    is CommonApiState.Success -> {
                        result.data.let {
                            Glide.with(requireContext()).load(it).into(binding.imageViewProfile)
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
     * viewModel.uploadFile 결과값 반영
     */
    private fun observeUploadS3ResultState() {
        lifecycleScope.launch {
            viewModel.uploadS3Result.collectLatest { result ->
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when (result) {
                    is CommonApiState.Success -> {
                        viewModel.updateMemberPhoto(viewModel.memberImageFileName!!)
                    }
                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    is CommonApiState.Loading -> showLoading()
                    is CommonApiState.Init -> {}
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
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when (result) {
                    is CommonApiState.Success -> {
                        viewModel.getMemberInfo()
                    }
                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    is CommonApiState.Loading -> showLoading()
                    is CommonApiState.Init -> {}
                }
            }
        }
    }
}