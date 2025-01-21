package com.petid.petid.viewmodel.generate

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petid.data.ml.ClassifierImageAnalyzer
import com.petid.data.ml.ClassifierResult
import com.petid.data.ml.ImageClassifierHelper
import com.petid.data.util.S3UploadHelper
import com.petid.domain.entity.Pet
import com.petid.domain.repository.PetInfoRepository
import com.petid.domain.util.ApiResult
import com.petid.petid.GlobalApplication.Companion.getPreferencesControl
import com.petid.petid.common.Constants
import com.petid.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class GeneratePetidSharedViewModel @Inject constructor(
    private val petInfoRepository: PetInfoRepository,
    private val imageAnalyzer: ClassifierImageAnalyzer,
    private val s3UploadHelper: S3UploadHelper,
): ViewModel() {
    var petInfo = Pet.Builder()
    var petImage : File? = null
    var signImage : File? = null
    val memberId = getPreferencesControl().getIntValue(Constants.SHARED_MEMBER_ID_VALUE)

    private val _registerPetResult = MutableStateFlow<CommonApiState<Unit>>(CommonApiState.Init)
    val registerPetResult = _registerPetResult.asStateFlow()

    /**
     * 애완동물 등록
     */
    private fun generatePetid() {
        viewModelScope.launch {
            _registerPetResult.emit(CommonApiState.Loading)
            val state = when (val result = petInfoRepository.registerPet(petInfo.build(true))) {
                is ApiResult.Success -> {
                    CommonApiState.Success(Unit)
                }
                is ApiResult.HttpError -> {
                    CommonApiState.Error(result.error.error)
                }
                is ApiResult.Error -> {
                    CommonApiState.Error(result.errorMessage)
                }
            }
            _registerPetResult.emit(state)
        }
    }

    /**
     * generatePetid를 위해 사전 이미지 업로드
     */
    fun uploadImageFiles() {
        viewModelScope.launch {
            _registerPetResult.emit(CommonApiState.Loading)

            runCatching {
                s3UploadHelper.uploadWithTransferUtility(file = petImage!!, keyName = petInfo.getPetImageName())
                s3UploadHelper.uploadWithTransferUtility(file = signImage!!, keyName = petInfo.getSignImageName())
            }.onSuccess {
                generatePetid()
            }.onFailure {
                _registerPetResult.emit(CommonApiState.Error(it.message))
            }
        }
    }

    /* 이미지 분석 결과 */
    private val _analysisState = MutableSharedFlow<AnalysisState>()
    val analysisState = _analysisState.asSharedFlow()

    /**
     * 이미지 분석
     */
    fun analyzeImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            _analysisState.emit(AnalysisState.Loading)

            // 이미지 변환
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(context.contentResolver, uri)
                )
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }.copy(Bitmap.Config.ARGB_8888, true)

            val state = when(val result = imageAnalyzer.analyzeImage(bitmap)) {
                is ClassifierResult.Success -> {
                    when(result.data.results.isNotEmpty()) {
                        true -> AnalysisState.Success(result.data)
                        false -> AnalysisState.Error("이미지 분석에 실패했습니다.")
                    }
                }
                is ClassifierResult.Error -> {
                    AnalysisState.Error(result.error.toString())
                }
            }
            _analysisState.emit(state)
        }
    }
}

sealed class AnalysisState {
    object Idle : AnalysisState()
    object Loading : AnalysisState()
    data class Success(val result: ImageClassifierHelper.ResultBundle) : AnalysisState()
    data class Error(val message: String) : AnalysisState()
}