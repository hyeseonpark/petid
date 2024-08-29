package com.android.data.repository

import com.android.data.source.remote.SocialAuthRemoteDataSource
import com.android.domain.entity.AuthEntity
import com.android.domain.repository.SocialAuthRepository
import com.android.domain.util.ApiResult
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
//                localDataSource.saveLogin(result.data)
                result
            }
            is ApiResult.Error -> result
        }
    }
}