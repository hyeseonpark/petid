package com.petid.data.source.remote

import com.petid.data.api.AuthAPI
import com.petid.data.api.MemberAPI
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
class SocialAuthRemoteDataSourceImpl @Inject constructor(
    private val authAPI: AuthAPI,
    private val memberAPI: MemberAPI
) : SocialAuthRemoteDataSource {

    override suspend fun getLogin(sub: String, fcmToken: String): ApiResult<AuthEntity> =
        runCatching {
            authAPI.login(sub, fcmToken).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun doRestore(): ApiResult<Unit> =
        runCatching {
            memberAPI.doRestore()
        }.mapApiResult { ApiResult.Success(it) }
}
