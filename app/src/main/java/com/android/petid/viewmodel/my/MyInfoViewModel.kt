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
    private val _updateMemberInfoResult = MutableSharedFlow<CommonApiState<Unit>>()
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

    /* 회원 탈퇴 */
    private val _doWithdrawResult = MutableSharedFlow<CommonApiState<Unit>>()
    val doWithdrawResult = _doWithdrawResult.asSharedFlow()

    /**
     * member 정보 가져오기
     */
    fun getMemberInfo() {
        // TODO 1. moshi, 2.stateIn()
        viewModelScope.launch {
            _getMemberInfoResult.emit(CommonApiState.Loading)
            val state = when (val result = myInfoRepository.getMemberInfo()) {
                is ApiResult.Success -> {
                    val memberInfo = result.data

                    memberImageFileName = "${PHOTO_PATHS[0]}${memberInfo.memberId}.jpg"
                    memberInfo.image?.also {
                        getMemberImage(it)
                    }

                    CommonApiState.Success(memberInfo)
                }
                is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            _getMemberInfoResult.emit(state)
        }
    }

    /**
     * 프로필 사진 가져오기 (S3 주소)
     */
    private fun getMemberImage(imageUrl: String) {
        viewModelScope.launch {
            _getMemberImageResult.emit(CommonApiState.Loading)
            val state = when (val result = myInfoRepository.getProfileImageUrl(imageUrl)) {
                is ApiResult.Success -> CommonApiState.Success(result.data)
                is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            _getMemberImageResult.emit(state)
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
            _updateMemberPhotoResult.emit(CommonApiState.Loading)
            val state = when (val result = myInfoRepository.updateMemberPhoto(filePath)) {
                is ApiResult.Success -> CommonApiState.Success(result.data)
                is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            _updateMemberPhotoResult.emit(state)
        }
    }

    /**
     * 회원 정보 업데이트
     */
    fun updateMemberInfo(address: String, addressDetails: String, phone: String) {
        viewModelScope.launch {
            _updateMemberInfoResult.emit(CommonApiState.Loading)
            val state = when (val result = myInfoRepository.updateMemberInfo(address, addressDetails, phone)) {
                is ApiResult.Success -> CommonApiState.Success(Unit)
                is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            _updateMemberInfoResult.emit(state)
        }
    }

    /**
     * 회원 탈퇴
     */
    fun doWithdraw() {
        viewModelScope.launch{
            _doWithdrawResult.emit(CommonApiState.Loading)
            val state = when(val result = myInfoRepository.doWithdraw()) {
                is ApiResult.Success -> CommonApiState.Success(Unit)
                is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            _doWithdrawResult.emit(state)
        }
    }
}