package com.petid.petid.ui.state

/**
 * Common UI State
 */
sealed class CommonUIState<out T> {
    data class Success<out T>(val data: T) : CommonUIState<T>()
    data class Error(val message: String?) : CommonUIState<Nothing>()
    object Loading : CommonUIState<Nothing>()
    object Init : CommonUIState<Nothing>()
}