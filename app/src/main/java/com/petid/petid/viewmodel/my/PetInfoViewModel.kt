package com.petid.petid.viewmodel.my

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petid.data.util.S3UploadHelper
import com.petid.domain.entity.PetDetailsEntity
import com.petid.domain.entity.PetUpdateEntity
import com.petid.domain.entity.UpdateAppearanceEntity
import com.petid.domain.repository.PetInfoRepository
import com.petid.domain.util.ApiResult
import com.petid.petid.common.Constants
import com.petid.petid.common.Constants.PHOTO_PATHS
import com.petid.petid.common.Constants.SHARED_MEMBER_ID_VALUE
import com.petid.petid.common.GlobalApplication.Companion.getPreferencesControl
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.ui.state.CommonApiState.*
import com.petid.petid.ui.state.CommonApiState.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PetInfoViewModel @Inject constructor(
    private val petInfoRepository: PetInfoRepository,
): ViewModel(){

    /* petId */
    private val petId: Long
        get() = getPreferencesControl().getIntValue(Constants.SHARED_PET_ID_VALUE).toLong()

    /* petImageId */
    private var petImageId: Long = -1

    /* 서버에서 받은 파일명, nullable */
    var petImageFileName: String? = null

    /* 펫 정보 가져오기 결과*/
    private val _getPetDetailsResult = MutableStateFlow<CommonApiState<PetDetailsEntity>>(
        Init
    )
    val getPetDetailsResult = _getPetDetailsResult.asStateFlow()

    /* 펫 이미지 가져오기 결과*/
    private val _getPetImageUrlResult = MutableStateFlow<CommonApiState<String>>(
        Init
    )
    val getPetImageUrlResult = _getPetImageUrlResult.asStateFlow()

    /* 서버 사진 update result */
    private val _updatePetPhotoResult = MutableStateFlow<CommonApiState<Unit>>(Init)
    val updatePetPhotoResult = _updatePetPhotoResult.asStateFlow()

    /* Pet info update result */
    private val _updatePetInfoResult = MutableSharedFlow<CommonApiState<Unit>>()
    val updatePetInfoResult = _updatePetInfoResult.asSharedFlow()

    /**
     * 펫 정보 가져오기
     */
    fun getPetDetails() {
        viewModelScope.launch {
            _getPetDetailsResult.emit(Loading)
            val state = when (val result = petInfoRepository.getPetDetails(petId)) {
                is ApiResult.Success -> {
                    val petDetails = result.data

                    getPetImageUrl(petDetails.petImages.first().imagePath)
                    petImageId = petDetails.petImages.first().petImageId.toLong()
                    petImageFileName = "${PHOTO_PATHS[2]}$petImageId.jpg"

                    Success(petDetails)
                }
                is ApiResult.HttpError -> Error(result.error.error)
                is ApiResult.Error -> Error(result.errorMessage)
            }
            _getPetDetailsResult.emit(state)
        }
    }

    /**
     * 펫 이미지 S3 주소 가져오기
     */
    private fun getPetImageUrl(filePath: String) {
        viewModelScope.launch {
            _getPetImageUrlResult.emit(Loading)
            val state = when (val result = petInfoRepository.getPetImageUrl(filePath)) {
                is ApiResult.Success -> Success(result.data)
                is ApiResult.HttpError -> Error(result.error.error)
                is ApiResult.Error -> Error(result.errorMessage)
            }
            _getPetImageUrlResult.emit(state)
        }
    }

    /**
     * S3 bucket upload
     */
    fun uploadFile(context: Context, file: File, fileName: String) {
        viewModelScope.launch {
            _updatePetPhotoResult.emit(Loading)
            val s3UploadHelper = S3UploadHelper()
            val result = s3UploadHelper.uploadWithTransferUtility(
                context = context,
                file = file,
                scope = this,
                keyName = fileName
            )
            result.fold(
                onSuccess = {
                    updatePetPhoto()
                },
                onFailure = { exception ->
                    _updatePetPhotoResult.emit(Error(exception.message))
                }
            )
        }
    }

    /**
     * 서버에 펫 프로필 사진 주소 업데이트
     */
    fun updatePetPhoto() {
        viewModelScope.launch {
            val petId = getPreferencesControl().getIntValue(Constants.SHARED_PET_ID_VALUE).toLong()
            val state = when (val result = petInfoRepository.updatePetPhoto(petId, petImageId, petImageFileName!!)) {
                is ApiResult.Success -> Success(result.data)
                is ApiResult.HttpError -> Error(result.error.error)
                is ApiResult.Error -> Error(result.errorMessage)
            }
            _updatePetPhotoResult.emit(state)
        }
    }

    /**
     * 펫 정보 업데이트
     */
    fun updatePetInfo(petNeuteredDate: String?, weight: Int?) {
        viewModelScope.launch {
            _updatePetInfoResult.emit(Loading)
            val updateData = PetUpdateEntity(
                petNeuteredDate,
                UpdateAppearanceEntity(weight)
            )
            val state = when (val result = petInfoRepository.updatePetInfo(petId, updateData)) {
                is ApiResult.Success -> Success(Unit)
                is ApiResult.HttpError -> Error(result.error.error)
                is ApiResult.Error -> Error(result.errorMessage)
            }
            _updatePetInfoResult.emit(state)
        }
    }
}