package com.android.petid.viewmodel.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.android.domain.entity.AuthEntity
import com.android.domain.usecase.login.DoLoginUseCase
import com.android.domain.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SocialAuthViewModel @Inject constructor(
    private val doLoginUseCase: DoLoginUseCase,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    suspend fun login(sub: String, fcmToken: String): ApiResult<AuthEntity> {
        return doLoginUseCase(sub, fcmToken)
    }

    /*val loginEvent = MutableLiveData<Event<LoginState>>()

    fun login(sub: String, fcmToken: String) {
        viewModelScope.launch {
            when (val result = getLoginUseCase(sub, fcmToken)) {
                is ApiResult.Success -> {
                    // 로그인 성공 처리
                    loginEvent.value = Event(LoginState.Success(result.data))
                }

                is ApiResult.Error -> {
                    val errorMessage = result.error.message
                    if (result.error is HttpException && result.error.code() == 404 &&
                        errorMessage?.contains("Member UID") == true
                    ) {
                        // 비회원 에러 처리: 회원가입 화면으로 이동하도록 상태 변경
                        loginEvent.value = Event(LoginState.NavigateToSignUp)
                    } else {
                        // 일반적인 에러 처리
                        loginEvent.value = Event(LoginState.Error(result.error))
                    }
                }
            }
        }
    }*/
}
