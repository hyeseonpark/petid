package com.android.data.source.remote

import com.android.data.api.AuthAPI
import com.android.data.dto.response.ErrorResponse
import com.android.data.dto.response.toDomain
import com.android.domain.entity.AuthEntity
import com.android.domain.util.ApiResult
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TermsRemoteDataSourceImpl @Inject constructor(
    private val authAPI: AuthAPI
) : TermsRemoteDataSource {
    override suspend fun doJoin(platform: String, sub: String, fcmToken: String, ad: Boolean
    ): ApiResult<AuthEntity> {
        return try {
            val response = authAPI.join(platform, sub, fcmToken, ad)
            ApiResult.Success(response.toDomain())

        } catch (e: HttpException) {
            val gson = Gson()
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            ApiResult.HttpError(errorResponse.toDomain())
        } catch (e: Exception) {
            ApiResult.Error(e.message)
        }
    }
}