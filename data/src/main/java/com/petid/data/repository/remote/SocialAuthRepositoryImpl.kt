package com.petid.data.repository.remote

import com.petid.data.source.remote.SocialAuthRemoteDataSource
import com.petid.domain.entity.AuthEntity
import com.petid.domain.repository.SocialAuthRepository
import com.petid.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialAuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: SocialAuthRemoteDataSource,
//    private val localDataSource: LoginLocalDataSource
) : SocialAuthRepository {

    override suspend fun doLogin(sub: String, fcmToken: String): ApiResult<AuthEntity> =
        // TODO localDataSource.saveLogin(result.data)
        remoteDataSource.getLogin(sub, fcmToken)

    override suspend fun doRestore(): ApiResult<Unit> =
        remoteDataSource.doRestore()
}