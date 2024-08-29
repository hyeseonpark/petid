package com.android.data.source.remote

import com.android.data.api.AuthAPI
import com.android.data.model.toDomain
import com.android.domain.entity.AuthEntity
import com.android.domain.util.ApiResult
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
            if (e.code() == 404) {
                val errorBody = e.response()?.errorBody()?.string()
                ApiResult.Error(Exception(errorBody))
            } else {
                ApiResult.Error(e)
            }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
}
