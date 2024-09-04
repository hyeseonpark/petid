package com.android.petid.ui.state

sealed class CommonApiState<out T> {
    data class Success<out T>(val data: T) : CommonApiState<T>()
    data class Error(val message: String?) : CommonApiState<Nothing>()
    object Loading : CommonApiState<Nothing>()
}