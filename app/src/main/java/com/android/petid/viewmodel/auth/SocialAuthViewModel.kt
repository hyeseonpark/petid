package com.android.petid.viewmodel.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.usecase.login.DoLoginUseCase
import com.android.domain.util.ApiResult
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

    fun login(sub: String, fcmToken: String) {
        viewModelScope.launch {
            _loginResult.emit(LoginResult.Loading)  // 로딩 상태 전송

            when (val result = doLoginUseCase(sub, fcmToken)) {
                is ApiResult.Success -> {
                    // TODO 응답값을 localRepository 를 통해 처리할수 있도록 바꾸기
                    _loginResult.emit(LoginResult.Success(result.data))  // 성공 시 데이터 전송
                }
                is ApiResult.Error -> {
                    if (result.error.status == 404 && result.error.error.contains("Member UID")) {
                        _loginResult.emit(LoginResult.NeedToSignUp)  // 회원가입 필요 시 전송
                    } else {
                        _loginResult.emit(LoginResult.Error(result.error.error))  // 오류 시 메시지 전송
                    }
                }
            }
        }
    }
}
