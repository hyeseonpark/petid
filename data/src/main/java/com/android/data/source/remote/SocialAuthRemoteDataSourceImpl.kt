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
class SocialAuthRemoteDataSourceImpl @Inject constructor(
    private val authAPI: AuthAPI
) : SocialAuthRemoteDataSource {

    override suspend fun getLogin(sub: String, fcmToken: String): ApiResult<AuthEntity> {
        return try {
            val response = authAPI.login(sub, fcmToken)
            ApiResult.Success(response.toDomain())

        } catch (e: HttpException) {
            if (e.code() == 400) {
                val gson = Gson()
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)

                ApiResult.HttpError(errorResponse.toDomain() )
            } else {
                ApiResult.Error(e.message)
            }

        } catch (e: Exception) {
            ApiResult.Error(e.message)
        }
    }
}
