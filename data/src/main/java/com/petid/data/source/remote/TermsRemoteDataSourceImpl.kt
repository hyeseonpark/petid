package com.petid.data.source.remote

import com.petid.data.api.AuthAPI
import com.petid.data.dto.response.ErrorResponse
import com.petid.data.dto.response.toDomain
import com.petid.domain.entity.AuthEntity
import com.petid.domain.util.ApiResult
import com.google.gson.Gson
import com.petid.data.util.mapApiResult
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TermsRemoteDataSourceImpl @Inject constructor(
    private val authAPI: AuthAPI
) : TermsRemoteDataSource {
    override suspend fun doJoin(platform: String, sub: String, fcmToken: String, ad: Boolean
    ): ApiResult<AuthEntity> =
        runCatching {
            authAPI.join(platform, sub, fcmToken, ad).toDomain()
        }.mapApiResult { ApiResult.Success(it) }
}