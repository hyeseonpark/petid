package com.android.petid.viewmodel.my

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.data.util.S3UploadHelper
import com.android.domain.entity.MemberInfoEntity
import com.android.domain.repository.MyInfoRepository
import com.android.domain.util.ApiResult
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
): ViewModel() {

    var profileImageName : String = ""

    /* Member info result */
    private val _getMemberInfoResult = MutableStateFlow<CommonApiState<MemberInfoEntity>>(
        CommonApiState.Loading
    )
    val getMemberInfoResult: StateFlow<CommonApiState<MemberInfoEntity>> = _getMemberInfoResult

    /* 서버에서 받은 파일명*/
    var memberImageFileName: String? = null

    /* image result: S3 */
    private val _getMemberImageResult = MutableSharedFlow<Result<String>>()
    val getMemberImageResult: SharedFlow<Result<String>> = _getMemberImageResult

    /* S3 upload helper 초기화 */
    private val s3UploadHelper = S3UploadHelper()

    /* S3 사진 upload result */
    private val _uploadS3Result = MutableSharedFlow<Result<Boolean>>()
    val uploadS3Result: SharedFlow<Result<Boolean>> get() = _uploadS3Result

    /* 서버 사진 update result */
    private val _updateMemberPhotoResult = MutableStateFlow<CommonApiState<String>>(
        CommonApiState.Loading
    )
    val updateMemberPhotoResult: StateFlow<CommonApiState<String>> get() = _updateMemberPhotoResult

    /**
     * member 정보 가져오기
     */
    fun getMemberInfo() {
        // TODO 1. moshi, 2.viewModel에서 repository 바로 연결시키기 (useCase 필요없), 3.stateIn()
        viewModelScope.launch {
            when (val result = myInfoRepository.getMemberInfo()) {
                is ApiResult.Success -> {
                    val memberInfo = result.data

                    memberInfo.image?.also {
                        memberImageFileName = it
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
        try {
            myInfoRepository.getProfileImageUrl(imageUrl).also {
                _getMemberImageResult.emit(Result.success(it))
            }
        } catch (e: Exception) {
            _getMemberImageResult.emit(Result.failure(e))
        }
    }


    /**
     * S3 bucket upload
     */
    fun uploadFile(context: Context, file: File, fileName: String) {
        s3UploadHelper.uploadWithTransferUtility(
            context = context,
            file = file,
            scope = viewModelScope,
            keyName = fileName
        ) { result ->
            _uploadS3Result.emit(result)
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
}