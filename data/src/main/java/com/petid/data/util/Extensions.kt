package com.petid.data.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.petid.data.dto.response.ErrorResponse
import com.petid.data.dto.response.toDomain
import com.petid.data.repository.local.DBResult
import com.petid.domain.util.ApiResult
import retrofit2.HttpException

/**
 * Result<T>를 ApiResult<T> 로 반환한다.
 * 이 때, onFailure 결과를 Exception 타입에 따라 ApiResult를 다르게 반환하는 작업을 공통화 하였다.
 */
inline fun <T> Result<T>.mapApiResult(transform: (T) -> ApiResult<T>): ApiResult<T> =
    fold(
        onSuccess = transform,
        onFailure = { error ->
            when (error) {
                is HttpException -> {
                    val errorBody = error.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    ApiResult.HttpError(errorResponse.toDomain())
                }
                else -> {
                    error.sendCrashlytics() // HTTPException 외 Exception 을 Crashlytics 기록하기 위함
                    ApiResult.Error(error.message)
                }
            }
        }
    )

/**
 * Result<T>를 DBResult<T>로 반환한다.
 */
inline fun <T> Result<T>.mapDBResult(transform : (T) -> DBResult<T>): DBResult<T> =
    fold(
        onSuccess = transform,
        onFailure = { error ->
            DBResult.Error(error)
        }
    )

/**
 * Crashlytics 기록
 */
fun Throwable.sendCrashlytics() {
    FirebaseCrashlytics.getInstance().recordException(this)
}
