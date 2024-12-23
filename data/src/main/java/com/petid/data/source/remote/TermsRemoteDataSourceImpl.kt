package com.petid.data.source.remote

import com.petid.data.api.AuthAPI
import com.petid.data.dto.response.ErrorResponse
import com.petid.data.dto.response.toDomain
import com.petid.domain.entity.AuthEntity
import com.petid.domain.util.ApiResult
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