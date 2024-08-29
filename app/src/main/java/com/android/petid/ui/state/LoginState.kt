package com.android.petid.ui.state

sealed class LoginState {
    data class Success<T>(val data: T) : LoginState()
    object ToSignUp : LoginState()
    data class Error(val error: Throwable?) : LoginState()
    object Loading : LoginState()
}