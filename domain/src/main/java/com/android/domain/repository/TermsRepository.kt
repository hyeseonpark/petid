package com.android.domain.repository

import com.android.domain.entity.AuthEntity
import com.android.domain.util.ApiResult

interface TermsRepository {
    suspend fun doJoin(platform: String, sub: String, fcmToken: String, ad: Boolean): ApiResult<AuthEntity>
}