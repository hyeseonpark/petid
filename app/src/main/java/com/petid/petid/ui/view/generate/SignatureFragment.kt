package com.petid.petid.ui.view.generate

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.petid.petid.R
import com.petid.petid.common.Constants
import com.petid.petid.common.Constants.PHOTO_PATHS
import com.petid.petid.common.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.common.GlobalApplication.Companion.getPreferencesControl
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.databinding.FragmentSignatureBinding
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.util.showErrorMessage
import com.petid.petid.util.toFile
import com.petid.petid.viewmodel.generate.GeneratePetidSharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SignatureFragment : BaseFragment<FragmentSignatureBinding>(FragmentSignatureBinding::inflate) {
    private val viewModel: GeneratePetidSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignatureBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
        )
        initComponent()
        observeUploadS3ResultState()
    }

    fun initComponent() {
        with(binding) {
            buttonRefresh.setOnClickListener{
                drawingViewSignature.clear()
            }
            buttonNext.setOnClickListener{
                val bitmap = getBitmapFromView(binding.drawingViewSignature)

                val memberId =
                    getPreferencesControl().getIntValue(Constants.SHARED_MEMBER_ID_VALUE)

                with(viewModel) {
                    // S3 서버에 올릴 파일 세팅
                    signImage = bitmap.toFile(getGlobalContext())
                    petInfo.setSign("${PHOTO_PATHS[1]}${memberId}.jpg")
                }

                viewModel.uploadImageFiles()
            }
        }
    }

    /**
     * viewModel.uploadFile 결과값 반영
     */
    private fun observeUploadS3ResultState() {
        lifecycleScope.launch {
            viewModel.registerPetResult.collectLatest { result ->
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when(result) {
                    is CommonApiState.Success -> {
                        Log.d("SignatureFragment", "success...")
                        findNavController().navigate(R.id.action_signatureFragment_to_completeCardFragment)
                    }
                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    CommonApiState.Init -> {}
                    CommonApiState.Loading -> showLoading()
                }
            }
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}