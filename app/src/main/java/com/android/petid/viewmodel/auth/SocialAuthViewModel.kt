package com.android.petid.viewmodel.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.usecase.login.DoLoginUseCase
import com.android.domain.util.ApiResult
import com.android.petid.common.Constants.SHARED_VALUE_ACCESS_TOKEN
import com.android.petid.common.Constants.SHARED_VALUE_REFRESH_TOKEN
import com.android.petid.common.GlobalApplication.Companion.getPreferencesControl
import com.android.petid.enum.PlatformType
import com.android.petid.ui.state.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialAuthViewModel @Inject constructor(
    private val doLoginUseCase: DoLoginUseCase,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _loginResult = MutableSharedFlow<LoginResult>()
    val loginResult: SharedFlow<LoginResult> = _loginResult

    var platform: PlatformType? = null
    var subValue: String? = null
    var fcmToken: String? = null

    fun login() {
        val sub = subValue ?: return  // subValue가 없으면 로그인 시도 안함
        val fcmToken = fcmToken ?: return  // FCM 토큰이 없으면 로그인 시도 안함

        viewModelScope.launch {
            _loginResult.emit(LoginResult.Loading)  // 로딩 상태 전송

            when (val result = doLoginUseCase(sub, fcmToken)) {
                is ApiResult.Success -> {
                    val result = result.data
                    getPreferencesControl().apply {
                        saveStringValue(SHARED_VALUE_ACCESS_TOKEN, result.accessToken.split(" ").last())
                        saveStringValue(SHARED_VALUE_REFRESH_TOKEN, result.refreshToken.split(" ").last())
                    }
                    _loginResult.emit(LoginResult.Success(result))  // 성공 시 데이터 전송
                }
                is ApiResult.HttpError -> {
                    if (result.error.status == 400 && result.error.error.contains("Member UID")) {
                        _loginResult.emit(LoginResult.NeedToSignUp)  // 회원가입 필요 시 전송
                    } else {
                        _loginResult.emit(LoginResult.Error(result.error.error))  // 오류 시 메시지 전송
                    }
                }
                is ApiResult.Error -> {
                    _loginResult.emit(LoginResult.Error(result.errorMessage))
                }
            }
        }
    }
}
