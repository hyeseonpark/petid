package com.petid.data.repository.remote

import com.petid.data.source.remote.TermsRemoteDataSource
import com.petid.domain.entity.AuthEntity
import com.petid.domain.repository.TermsRepository
import com.petid.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TermsRepositoryImpl @Inject constructor(
    private val remoteDataSource: TermsRemoteDataSource
) : TermsRepository{
    override suspend fun doJoin(platform: String, sub: String, fcmToken: String, ad: Boolean
    ): ApiResult<AuthEntity> =
        remoteDataSource.doJoin(platform, sub, fcmToken, ad)
}