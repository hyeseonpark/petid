package com.petid.data.source.remote

import com.petid.data.api.AuthAPI
import com.petid.data.api.MemberAPI
import com.petid.data.dto.response.ErrorResponse
import com.petid.data.dto.response.toDomain
import com.petid.domain.entity.AuthEntity
import com.petid.domain.util.ApiResult
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SocialAuthRemoteDataSourceImpl @Inject constructor(
    private val authAPI: AuthAPI,
    private val memberAPI: MemberAPI
) : SocialAuthRemoteDataSource {

    override suspend fun getLogin(sub: String, fcmToken: String): ApiResult<AuthEntity> {
        return try {
            val response = authAPI.login(sub, fcmToken)
            ApiResult.Success(response.toDomain())

        } catch (e: HttpException) {
            if (e.code() in 400..401) {
                val gson = Gson()
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)

                ApiResult.HttpError(errorResponse.toDomain())
            } else {
                ApiResult.Error(e.message)
            }

        } catch (e: Exception) {
            ApiResult.Error(e.message)
        }
    }

    override suspend fun doRestore(): ApiResult<Unit> {
        return try {
            val response = memberAPI.doRestore()
            ApiResult.Success(response)
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
