package com.android.petid.ui.view.generate

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.databinding.FragmentSignatureBinding

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
        binding.buttonNext.setOnClickListener{
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
    /**
     * viewModel.uploadFile 결과값 반영
     */
    private fun observeUploadS3ResultState() {
        lifecycleScope.launch {
            viewModel.registerPetResult.collectLatest { result ->
                when(result) {
                    is CommonApiState.Success -> {
                        findNavController().navigate(R.id.action_signatureFragment_to_completeCardFragment)
                    }
                    is CommonApiState.Error -> {}
                    CommonApiState.Init -> {}
                    CommonApiState.Loading -> {}
                }
            }
        }
    }

    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}
// 서버 전송 시
//import android.util.Base64
//import java.io.ByteArrayOutputStream
//
//fun bitmapToBase64(bitmap: Bitmap): String {
//    val byteArrayOutputStream = ByteArrayOutputStream()
//    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//    val byteArray = byteArrayOutputStream.toByteArray()
//    return Base64.encodeToString(byteArray, Base64.DEFAULT)
//}
//
//fun sendBitmapToServer(base64Bitmap: String) {
//    // Volley 라이브러리를 사용한 예시
//    val url = "https://yourserver.com/upload"
//    val request = object : StringRequest(Method.POST, url,
//        Response.Listener { response ->
//            // 서버 응답 처리
//        },
//        Response.ErrorListener { error ->
//            // 오류 처리
//        }) {
//        override fun getParams(): Map<String, String> {
//            val params = HashMap<String, String>()
//            params["image"] = base64Bitmap
//            return params
//        }
//    }
//    val queue = Volley.newRequestQueue(context)
//    queue.add(request)
//}