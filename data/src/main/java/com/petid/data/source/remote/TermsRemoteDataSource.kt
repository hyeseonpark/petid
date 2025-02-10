package com.petid.data.source.remote

import com.petid.domain.entity.AuthEntity
import com.petid.domain.util.ApiResult

interface TermsRemoteDataSource {
    suspend fun doJoin(platform: String, token: String, fcmToken: String, ad: Boolean): ApiResult<AuthEntity>
}