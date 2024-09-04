package com.android.data.source.remote

import com.android.domain.entity.AuthEntity
import com.android.domain.util.ApiResult

interface TermsRemoteDataSource {
    suspend fun doJoin(platform: String, sub: String, fcmToken: String, ad: Boolean): ApiResult<AuthEntity>
}