package com.android.data.source.remote

import com.android.data.api.AuthAPI
import com.android.data.model.ErrorResponse
import com.android.data.model.toDomain
import com.android.domain.entity.AuthEntity
import com.android.domain.entity.ErrorEntity
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
            val gson = Gson()
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)

            ApiResult.Error(errorResponse.toDomain())

        } catch (e: Exception) {
            val gson = Gson()
            val errorBody = e.toString()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            ApiResult.Error(errorResponse.toDomain())
        }
    }
}
