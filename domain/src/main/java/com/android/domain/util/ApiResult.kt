package com.android.domain.util

sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val error: Throwable) : ApiResult<T>()
}

inline fun <T, R> ApiResult<T>.getResult(
    success: (ApiResult.Success<T>) -> R,
    error: (ApiResult.Error<T>) -> R
): R = when (this) {
    is ApiResult.Success -> success(this)
    is ApiResult.Error -> error(this)
}

inline fun <T> ApiResult<T>.onSuccess(
    action: (T) -> Unit
): ApiResult<T> {
    if (this is ApiResult.Success) action(data)
    return this
}

inline fun <T> ApiResult<T>.onError(
    action: (Throwable) -> Unit
): ApiResult<T> {
    if (this is ApiResult.Error) action(error)
    return this
}