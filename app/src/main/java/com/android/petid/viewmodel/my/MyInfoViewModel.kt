package com.android.petid.viewmodel.my

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.data.util.S3UploadHelper
import com.android.domain.entity.MemberInfoEntity
import com.android.domain.repository.MyInfoRepository
import com.android.domain.util.ApiResult
import com.android.petid.common.Constants.PHOTO_PATHS
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
): ViewModel() {

    /* 서버에서 받은 파일명, nullable */
    var memberImageFileName: String? = null

    /* Member info result */
    private val _getMemberInfoResult = MutableStateFlow<CommonApiState<MemberInfoEntity>>(
        CommonApiState.Init
    )
    val getMemberInfoResult = _getMemberInfoResult.asStateFlow()

    /* Member info update result */
    private val _updateMemberInfoResult = MutableSharedFlow<CommonApiState<Boolean>>()
    val updateMemberInfoResult = _updateMemberInfoResult.asSharedFlow()


    /* image result: S3 */
    private val _getMemberImageResult = MutableStateFlow<CommonApiState<String>>(CommonApiState.Init)
    val getMemberImageResult = _getMemberImageResult.asStateFlow()

    /* S3 사진 upload result */
    private val _uploadS3Result = MutableSharedFlow<Result<Boolean>>()
    val uploadS3Result = _uploadS3Result.asSharedFlow()

    /* 서버 사진 update result */
    private val _updateMemberPhotoResult = MutableStateFlow<CommonApiState<String>>(CommonApiState.Init)
    val updateMemberPhotoResult = _updateMemberPhotoResult.asStateFlow()

    /**
     * member 정보 가져오기
     */
    fun getMemberInfo() {
        // TODO 1. moshi, 2.viewModel에서 repository 바로 연결시키기 (useCase 필요없), 3.stateIn()
        viewModelScope.launch {
            when (val result = myInfoRepository.getMemberInfo()) {
                is ApiResult.Success -> {
                    val memberInfo = result.data

                    memberImageFileName = "${PHOTO_PATHS[0]}${memberInfo.memberId}.jpg"
                    memberInfo.image?.also {
                        getMemberImage(it)
                    }

                    _getMemberInfoResult.emit(CommonApiState.Success(memberInfo))
                }
                is ApiResult.HttpError -> {
                    _getMemberInfoResult.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _getMemberInfoResult.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }

    /**
     * 프로필 사진 가져오기 (S3 주소)
     */
    private suspend fun getMemberImage(imageUrl: String) {
        viewModelScope.launch {
            when (val result = myInfoRepository.getProfileImageUrl(imageUrl)) {
                is ApiResult.Success -> {
                    _getMemberImageResult.emit(CommonApiState.Success(result.data))
                }
                is ApiResult.HttpError -> {
                    _getMemberImageResult.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _getMemberImageResult.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }


    /**
     * S3 bucket upload
     */
    fun uploadFile(context: Context, file: File, fileName: String) {
        viewModelScope.launch {
            val s3UploadHelper = S3UploadHelper()
            val result = s3UploadHelper.uploadWithTransferUtility(
                context = context,
                file = file,
                scope = this,
                keyName = fileName
            )
            result.fold(
                onSuccess = {
                    _uploadS3Result.emit(result)
                },
                onFailure = { exception ->
                    _uploadS3Result.emit(Result.failure(exception))
                }
            )
        }
    }

    /**
     * 서버에 프로필 사진 주소 업데이트
     */
    fun updateMemberPhoto(filePath: String) {
        viewModelScope.launch {
            when (val result = myInfoRepository.updateMemberPhoto(filePath)) {
                is ApiResult.Success -> {
                    _updateMemberPhotoResult.emit(CommonApiState.Success(result.data))
                }
                is ApiResult.HttpError -> {
                    _updateMemberPhotoResult.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _updateMemberPhotoResult.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }

    /**
     * 회원 정보 업데이트
     */
    fun updateMemberInfo(address: String, addressDetails: String, phone: String) {
        viewModelScope.launch {
            when (val result = myInfoRepository.updateMemberInfo(address, addressDetails, phone)) {
                is ApiResult.Success -> {
                    _updateMemberInfoResult.emit(CommonApiState.Success(true))
                }
                is ApiResult.HttpError -> {
                    _updateMemberInfoResult.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _updateMemberInfoResult.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }
}