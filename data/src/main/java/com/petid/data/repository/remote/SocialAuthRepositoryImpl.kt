package com.petid.data.repository.remote

import com.petid.data.dto.response.toDomain
import com.petid.data.source.remote.SocialAuthRemoteDataSource
import com.petid.data.util.mapApiResult
import com.petid.domain.entity.AuthEntity
import com.petid.domain.repository.SocialAuthRepository
import com.petid.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialAuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: SocialAuthRemoteDataSource,
) : SocialAuthRepository {

    override suspend fun doLogin(sub: String, fcmToken: String): ApiResult<AuthEntity> =
        runCatching {
            remoteDataSource.getLogin(sub, fcmToken).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun doRestore(): ApiResult<Unit> =
        runCatching {
            remoteDataSource.doRestore()
        }.mapApiResult { ApiResult.Success(it) }
}