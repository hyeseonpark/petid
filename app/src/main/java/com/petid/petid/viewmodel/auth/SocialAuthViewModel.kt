package com.petid.petid.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petid.data.util.Constants.SHARED_VALUE_ACCESS_TOKEN
import com.petid.data.util.Constants.SHARED_VALUE_REFRESH_TOKEN
import com.petid.domain.repository.SocialAuthRepository
import com.petid.domain.util.ApiResult
import com.petid.petid.GlobalApplication.Companion.getPreferencesControl
import com.petid.petid.type.PlatformType
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.ui.state.CommonApiState.*
import com.petid.petid.ui.state.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialAuthViewModel @Inject constructor(
    private val socialAuthRepository : SocialAuthRepository,
): ViewModel() {

    var platform: PlatformType? = null
    var subValue: String? = null
    var fcmToken: String? = null

    /* login result state*/
    private val _loginResult = MutableSharedFlow<LoginResult>()
    val loginResult = _loginResult.asSharedFlow()

    /* restore result state*/
    private val _restoreResult = MutableSharedFlow<CommonApiState<Unit>>()
    val restoreResult = _restoreResult.asSharedFlow()

    /**
     * 로그인
     */
    fun login() {
        val sub = subValue ?: return  // subValue가 없으면 로그인 시도 안함
        val fcmToken = fcmToken ?: return  // FCM 토큰이 없으면 로그인 시도 안함

        viewModelScope.launch {
            _loginResult.emit(LoginResult.Loading)  // 로딩 상태 전송

            val state = when (val result = socialAuthRepository.doLogin(sub, fcmToken)) {
                is ApiResult.Success -> {
                    val result = result.data
                    getPreferencesControl().apply {
                        saveStringValue(SHARED_VALUE_ACCESS_TOKEN, result.accessToken.split(" ").last())
                        saveStringValue(SHARED_VALUE_REFRESH_TOKEN, result.refreshToken.split(" ").last())
                    }
                    LoginResult.Success(result)  // 성공 시 데이터 전송
                }
                is ApiResult.HttpError -> {
                    when(result.error.status) {
                        400 -> LoginResult.NeedToSignUp // 회원가입 필요 시 전송
                        401 -> LoginResult.TryToRestore // 재가입 시도
                        else -> LoginResult.Error(result.error.error)
                    }
                }
                is ApiResult.Error -> {
                    LoginResult.Error(result.errorMessage)
                }
            }
            _loginResult.emit(state)
        }
    }

    /**
     * 계정 복구
     */
    fun doRestore() {
        viewModelScope.launch {
            _restoreResult.emit(Loading)
            val state = when (val result = socialAuthRepository.doRestore()) {
                is ApiResult.Success -> Success(Unit)
                is ApiResult.HttpError -> Error(result.error.error)
                is ApiResult.Error -> Error(result.errorMessage)
            }
            _restoreResult.emit(state)
        }
    }
}
