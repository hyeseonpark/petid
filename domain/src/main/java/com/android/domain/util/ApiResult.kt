package com.android.domain.util

import com.android.domain.entity.ErrorEntity

sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class HttpError<T>(val error: ErrorEntity) : ApiResult<T>()
    data class Error<T>(val errorMessage: String?) : ApiResult<T>()
}

inline fun <T, R> ApiResult<T>.getResult(
    success: (ApiResult.Success<T>) -> R,
    error: (ApiResult.HttpError<T>) -> R,
): R = when (this) {
    is ApiResult.Success -> success(this)
    is ApiResult.HttpError -> error(this)
    is ApiResult.Error -> error(this)
}

inline fun <T> ApiResult<T>.onSuccess(
    action: (T) -> Unit
): ApiResult<T> {
    if (this is ApiResult.Success) action(data)
    return this
}

inline fun <T> ApiResult<T>.onError(
    action: (String?) -> Unit
): ApiResult<T> {
    if (this is ApiResult.Error) action(errorMessage)
    return this
}