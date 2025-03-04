package com.petid.domain.util

import com.petid.domain.entity.ErrorEntity

sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class HttpError<T>(val error: ErrorEntity) : ApiResult<T>()
    data class Error<T>(val errorMessage: String?) : ApiResult<T>()
}
