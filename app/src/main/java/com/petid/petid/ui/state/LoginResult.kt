package com.petid.petid.ui.state

import com.petid.domain.entity.AuthEntity

/**
 * Login REST State Values
 */
sealed class LoginResult {
    data class Success(val data: AuthEntity) : LoginResult()
    data class Error(val message: String?) : LoginResult()
    object NeedToSignUp : LoginResult()
    object TryToRestore: LoginResult()
    object Loading : LoginResult()
}