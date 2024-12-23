package com.petid.data.repository

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

    override suspend fun doLogin(sub: String, fcmToken: String): ApiResult<AuthEntity> {
        return when (val result = remoteDataSource.getLogin(sub, fcmToken)) {
            is ApiResult.Success -> {
                // TODO
//                localDataSource.saveLogin(result.data)
                result
            }
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun doRestore(): ApiResult<Unit> {
        return when (val result = remoteDataSource.doRestore()) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }
}