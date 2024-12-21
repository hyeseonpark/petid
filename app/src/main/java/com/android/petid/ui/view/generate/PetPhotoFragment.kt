package com.android.petid.ui.view.generate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.petid.BuildConfig
import com.android.petid.R
import com.android.petid.common.Constants
import com.android.petid.common.Constants.PHOTO_PATHS
import com.android.petid.common.GlobalApplication.Companion.getPreferencesControl
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.databinding.FragmentPetPhotoBinding
import com.android.petid.image_classifier.ImageClassifierHelper
import com.android.petid.ui.component.CustomDialogCommon
import com.android.petid.util.Utils.bitmapToFile
import com.android.petid.viewmodel.generate.GeneratePetidSharedViewModel
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifierResult
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

@AndroidEntryPoint
class PetPhotoFragment : BaseFragment<FragmentPetPhotoBinding>(FragmentPetPhotoBinding::inflate),
    ImageClassifierHelper.ClassifierListener {

    private val viewModel: GeneratePetidSharedViewModel by activityViewModels()

    private val TAG = this.javaClass.simpleName

    // 카메라 권한 요청 결과값 처리
    private val cameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            when(it) {
                true -> takePictureFullSize()
                false -> {}  // 사용자가 권한 거부시
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetPhotoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
        )
        initComponent()
    }

    fun initComponent() {
        with(binding) {
            buttonTakingPhoto.setOnClickListener{
                val cameraPermissionCheck = ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                )

                if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED) { // 촬영 권한 없는 경우
                    cameraPermissionResult.launch(android.Manifest.permission.CAMERA)
                } else {
                    takePictureFullSize()
                }
            }

            if (BuildConfig.DEBUG) {
                textViewCurrentStep.setOnClickListener{
                    findNavController().navigate(R.id.action_petPhotoFragment_to_scannedInfoFragment)
                }
            }
        }
    }

    /**
     * 모델 추론 후 결과값 처리
     */
    private fun handleResult(result: ImageClassifierResult) {
        val classifierResult = result.classificationResult().classifications()
        try {
            if (classifierResult.isNotEmpty()) {
                val breed = classifierResult[0].categories()[0]?.categoryName() ?: ""
                val hairLength = classifierResult[1].categories()[0]?.categoryName() ?: ""
                val weight = try {
                    classifierResult[2].categories()[0]?.categoryName()!!.toInt()
                } catch (_: Exception) {
                    0
                }
                val hairColor = classifierResult[3].categories()[0].categoryName() ?: ""

                viewModel.petInfo.setAppearance(breed, hairColor, weight, hairLength)

                findNavController().navigate(R.id.action_petPhotoFragment_to_scannedInfoFragment)
            }
        } catch (e: Exception) {
            CustomDialogCommon(
                title = getString(R.string.pet_photo_activity_dialog_retry_title),
                isSingleButton = true,
                singleButtonText = getString(R.string.confirm)
            ).show(childFragmentManager, null)
        }
    }

    // 촬영한 사진 저장 uri
    private lateinit var photoURI: Uri

    // 사진 촬영을 위한 launcher
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // 사진 촬영 성공 시 처리
                when (loadMediaType(photoURI)) {
                    MediaType.IMAGE -> runClassificationOnImage(photoURI)
                    MediaType.UNKNOWN -> {
                        Toast.makeText(
                            requireContext(),
                            "Unsupported data type.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } else {
                // 사진 촬영 실패 시 처리
                Toast.makeText(context, "사진 촬영에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    /**
     * 사진 촬영을 시작하는 함수
     */
    private fun takePictureFullSize() {
        photoURI = Uri.EMPTY
        val fullSizePictureIntent = getPictureIntent_App_Specific(requireContext())
        takePictureLauncher.launch(fullSizePictureIntent)
    }

    /**
     * 카메라 호출할 Intent 생성
     */
    private fun getPictureIntent_App_Specific(context: Context): Intent {
        val fullSizeCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // 파일 생성 - 촬영 사진이 저장될 위치
        val photoFile: File? = try {
            createImageFile(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }

        photoFile?.also {
            // 생성된 File로부터 Uri 생성 (by FileProvider)
            photoURI = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                it
            )
            // 생성된 Uri를 Intent에 추가
            fullSizeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        }
        return fullSizeCaptureIntent
    }

    /**
     * 빈 파일 생성
     */
    @Throws(IOException::class)
    private fun createImageFile(storageDir: File?): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            Log.i("syTest", "Created File AbsolutePath : $absolutePath")
        }
    }


    enum class MediaType {
        IMAGE, UNKNOWN
    }

    // 기본 세팅
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    /** Blocking ML operations are performed using this executor */
    private lateinit var backgroundExecutor: ScheduledExecutorService

    // Load and display the image.
    private fun runClassificationOnImage(uri: Uri) {
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(
                requireActivity().contentResolver, uri
            )
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver, uri
            )
        }.copy(Bitmap.Config.ARGB_8888, true)?.let { bitmap ->
            val memberId =
                getPreferencesControl().getIntValue(Constants.SHARED_MEMBER_ID_VALUE)

            with(viewModel) {
                // S3 서버에 올릴 파일 세팅
                petImage = bitmapToFile(requireContext(), bitmap, "pet_image.jpg")
                petInfo.setPetImage("${PHOTO_PATHS[2]}${memberId}.jpg")
            }

            // Run image classification on the input image
            backgroundExecutor.execute {
                imageClassifierHelper = ImageClassifierHelper(
                    context = requireContext(),
                    imageClassifierListener = this
                )
                imageClassifierHelper.classifyImage(bitmap)?.let { resultBundle ->
                    Log.d(TAG, resultBundle.results.toString())
                    activity?.runOnUiThread {
                        val result = resultBundle.results.first() // 결과값
                        Log.d(TAG, result.toString())

                        handleResult(result)
                    }
                } ?: run {
                    Log.e(TAG, "Error running image classification.")
                }

                imageClassifierHelper.clearImageClassifier()
            }
        }
    }

    // Check the type of media that user selected.
    private fun loadMediaType(uri: Uri): MediaType {
        val mimeType = context?.contentResolver?.getType(uri)
        mimeType?.let {
            if (mimeType.startsWith("image")) return MediaType.IMAGE
        }

        return MediaType.UNKNOWN
    }

    private fun classifyingError() {
        activity?.runOnUiThread {
            //fragmentGalleryBinding.progress.visibility = View.GONE
        }
    }

    override fun onError(error: String, errorCode: Int) {
        classifyingError()
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            if (errorCode == ImageClassifierHelper.GPU_ERROR) {
                Log.d(TAG, "GPU ERROR...")
            }
        }
    }

    override fun onResults(resultBundle: ImageClassifierHelper.ResultBundle) {
        // no-op
    }
}