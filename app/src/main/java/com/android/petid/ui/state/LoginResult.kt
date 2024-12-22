package com.android.petid.ui.state

import com.android.domain.entity.AuthEntity

sealed class LoginResult {
    data class Success(val data: AuthEntity) : LoginResult()
    data class Error(val message: String?) : LoginResult()
    object NeedToSignUp : LoginResult()
    object TryToRestore: LoginResult()
    object Loading : LoginResult()
}