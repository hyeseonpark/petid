package com.android.petid.ui.view.generate

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
import com.android.petid.R
import com.android.petid.common.Constants
import com.android.petid.common.Constants.PHOTO_PATHS
import com.android.petid.common.GlobalApplication.Companion.getPreferencesControl
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.databinding.FragmentSignatureBinding
import com.android.petid.ui.state.CommonApiState
import com.android.petid.util.Utils.bitmapToFile
import com.android.petid.viewmodel.generate.GeneratePetidSharedViewModel
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
                    signImage = bitmapToFile(requireContext(), bitmap, "signature.jpg")
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
                when(result) {
                    is CommonApiState.Success -> {
                        Log.d("SignatureFragment", "success...")
                        findNavController().navigate(R.id.action_signatureFragment_to_completeCardFragment)
                    }
                    is CommonApiState.Error -> {
                        Log.d("SignatureFragment", "error...: ${result.message}")
                    }
                    CommonApiState.Init -> {
                        Log.d("SignatureFragment", "init...")
                    }
                    CommonApiState.Loading -> {
                        Log.d("SignatureFragment", "loading...")
                    }
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