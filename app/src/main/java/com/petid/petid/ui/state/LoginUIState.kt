package com.petid.petid.ui.state

import com.petid.domain.entity.AuthEntity

/**
 * Login UI State
 */
sealed class LoginUIState {
    data class Success(val data: AuthEntity) : LoginUIState()
    data class Error(val message: String?) : LoginUIState()
    object NeedToSignUp : LoginUIState()
    object TryToRestore: LoginUIState()
    object Loading : LoginUIState()
}