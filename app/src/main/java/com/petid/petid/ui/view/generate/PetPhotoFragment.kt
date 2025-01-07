package com.petid.petid.ui.view.generate

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.mediapipe.tasks.components.containers.Classifications
import com.petid.petid.BuildConfig
import com.petid.petid.R
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.databinding.FragmentPetPhotoBinding
import com.petid.petid.ui.component.CustomDialogCommon
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.util.TAG
import com.petid.petid.util.showErrorMessage
import com.petid.petid.util.throttleFirst
import com.petid.petid.viewmodel.generate.AnalysisState
import com.petid.petid.viewmodel.generate.GeneratePetidSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.round

@AndroidEntryPoint
class PetPhotoFragment : BaseFragment<FragmentPetPhotoBinding>(FragmentPetPhotoBinding::inflate) {

    private val viewModel: GeneratePetidSharedViewModel by activityViewModels()

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

        observeAnalysisState()
    }

    fun initComponent() {
        with(binding) {
            buttonTakingPhoto
                .clicks()
                .throttleFirst()
                .onEach {
                    val cameraPermissionCheck = ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    )

                    if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED) { // 촬영 권한 없는 경우
                        cameraPermissionResult.launch(Manifest.permission.CAMERA)
                    } else {
                        takePictureFullSize()
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            if (BuildConfig.DEBUG) {
                textViewCurrentStep
                    .clicks()
                    .throttleFirst()
                    .onEach {
                        findNavController().navigate(R.id.action_petPhotoFragment_to_scannedInfoFragment)
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }
    }

    // 촬영한 사진 저장 uri
    private lateinit var photoURI: Uri

    // 사진 촬영을 위한 launcher
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.analyzeImage(getGlobalContext(), photoURI)
            } else {
                showErrorMessage("사진 촬영 실패, resultCode: ${result.resultCode}")
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
            Log.i(TAG, "Created File AbsolutePath : $absolutePath")
        }
    }

    /**
     * observe 이미지 분석
     */
    private fun observeAnalysisState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.analysisState.collect { result ->
                if (result !is AnalysisState.Loading)
                    hideLoading()

                when(result) {
                    AnalysisState.Idle -> {}
                    AnalysisState.Loading -> showLoading()
                    is AnalysisState.Success -> {
                        val classifierResult = result.result.results[0].classificationResult().classifications()

                        val breed = classifierResult.getCategoryName(0)
                        val hairLength = classifierResult.getCategoryName(1)

                        val weight =
                            classifierResult.getOrNull(2)?.categories()?.firstOrNull()?.score()?.let {
                                round(it*10)/10
                            } ?: 0
                        val hairColor = classifierResult.getCategoryName(3)

                        // TODO weight 값 백엔드에서 수정완료 시 반영
                        viewModel.petInfo.setAppearance(breed, hairColor, weight.toInt(), hairLength)

                        findNavController().navigate(R.id.action_petPhotoFragment_to_scannedInfoFragment)
                    }
                    is AnalysisState.Error -> {
                        retryDialog().show(childFragmentManager, null)
                    }
                }
            }
        }
    }

    /**
     * 재시도 dialog
     */
    private fun retryDialog() = CustomDialogCommon(
        title = getString(R.string.pet_photo_activity_dialog_retry_title),
        isSingleButton = true,
        singleButtonText = getString(R.string.confirm)
    )

    private fun List<Classifications>.getCategoryName(index: Int): String =
        this.getOrNull(index)?.categories()?.firstOrNull()?.categoryName().orEmpty()
}
