package com.petid.petid.viewmodel.my

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.petid.data.util.PreferencesHelper
import com.petid.data.util.S3UploadHelper
import com.petid.data.util.sendCrashlytics
import com.petid.domain.entity.MemberInfoEntity
import com.petid.domain.repository.MyInfoRepository
import com.petid.domain.util.ApiResult
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.GlobalApplication.Companion.getPreferencesControl
import com.petid.petid.common.Constants
import com.petid.petid.common.Constants.PHOTO_PATHS
import com.petid.petid.common.Constants.SHARED_AUTH_PROVIDER
import com.petid.petid.type.PlatformType
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.util.showErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class MyInfoViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
    private val s3UploadHelper: S3UploadHelper,
    private val preferencesHelper: PreferencesHelper,
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
    private val _uploadS3Result = MutableSharedFlow<CommonApiState<Unit>>()
    val uploadS3Result = _uploadS3Result.asSharedFlow()

    /* 서버 사진 update result */
    private val _updateMemberPhotoResult = MutableStateFlow<CommonApiState<String>>(CommonApiState.Init)
    val updateMemberPhotoResult = _updateMemberPhotoResult.asStateFlow()

    /* 로그아웃 result */
    private val _doLogoutResult = MutableSharedFlow<CommonApiState<Unit>>()
    val doLogoutResult = _doLogoutResult.asSharedFlow()

    /* 회원 탈퇴 result */
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
    fun uploadFile(file: File, fileName: String) {
        viewModelScope.launch {
            _uploadS3Result.emit(CommonApiState.Loading)

            runCatching {
                s3UploadHelper.uploadWithTransferUtility(file = file, keyName = fileName)
            }.onSuccess {
                _uploadS3Result.emit(CommonApiState.Success(Unit))
            }.onFailure { e ->
                e.sendCrashlytics()
                _uploadS3Result.emit(CommonApiState.Error(e.message))
            }
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
     * 로그아웃
     */
    fun doLogout() {
        viewModelScope.launch{
            _doLogoutResult.emit(CommonApiState.Loading)
            val authProvider = getPreferencesControl().getStringValue(SHARED_AUTH_PROVIDER)

            runCatching {
                when(PlatformType.fromValue(authProvider)) {
                    PlatformType.naver -> {
                        if(NaverIdLoginSDK.isInitialized()) {
                            NaverIdLoginSDK.logout()
                        }
                    }
                    PlatformType.google -> {
                        CredentialManager.create(getGlobalContext()).clearCredentialState(request = ClearCredentialStateRequest())
                        FirebaseAuth.getInstance().signOut()
                    }
                    PlatformType.kakao -> {
                        UserApiClient.instance.logout { error ->
                            if (error != null) {
                                throw Exception("카카오 로그아웃 실패: ${error.message}")
                            }
                        }
                    }
                    null -> {}
                }
            }.onSuccess {
                preferencesHelper.run {
                    clear()
                    saveBooleanValue(Constants.SHARED_VALUE_IS_FIRST_RUN, false)
                }
                _doLogoutResult.emit(CommonApiState.Success(Unit))
            }.onFailure { exception ->
                _doLogoutResult.emit(CommonApiState.Error(exception.message ?: "로그아웃 실패"))
            }
        }
    }

    /**
     * 회원탈퇴: 소셜로그인
     */
    fun doWithdrawSocialAuth() {
        viewModelScope.launch{
            _doLogoutResult.emit(CommonApiState.Loading)
            val authProvider = getPreferencesControl().getStringValue(SHARED_AUTH_PROVIDER)

            val result = runCatching {
                when(PlatformType.fromValue(authProvider)) {
                    PlatformType.naver -> deleteNaverToken()
                    PlatformType.google -> {
                        try {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            if (currentUser != null) {
                                Tasks.await(currentUser.delete()) // 코루틴 내에서 await() 사용
                            }
                            CredentialManager.create(getGlobalContext()).clearCredentialState(ClearCredentialStateRequest())
                        } catch (e: Exception) {
                            throw Exception("[구글] 회원탈퇴 실패: ${e.message}")
                        }
                    }
                    PlatformType.kakao -> {
                        UserApiClient.instance.unlink { error ->
                            if (error != null) {
                                throw Exception("[카카오] 회원탈퇴 실패: ${error.message}")
                            }
                        }
                    }
                    null -> throw Exception("로그인된 플랫폼이 없음")
                }
            }.onFailure { exception ->
                _doLogoutResult.emit(CommonApiState.Error(exception.message ?: "회원탈퇴 실패"))
            }

            if (result.isSuccess) {
                preferencesHelper.run {
                    clear()
                    saveBooleanValue(Constants.SHARED_VALUE_IS_FIRST_RUN, false)
                }
                _doLogoutResult.emit(CommonApiState.Success(Unit))
            }
        }
    }

    /**
     *
     */
    private suspend fun deleteNaverToken(): Result<Unit> = suspendCoroutine { continuation ->
        NidOAuthLogin().callDeleteTokenApi(object : OAuthLoginCallback {
            override fun onSuccess() {
                continuation.resume(Result.success(Unit))
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorMessage = "네이버 토큰 삭제 실패 (HTTP 상태 코드: $httpStatus, 메시지: $message)"
                continuation.resume(Result.failure(Exception(errorMessage)))
            }

            override fun onError(errorCode: Int, message: String) {
                val errorMessage = "네이버 토큰 삭제 실패 (에러 코드: $errorCode, 메시지: $message)"
                continuation.resume(Result.failure(Exception(errorMessage)))
            }
        })
    }

}