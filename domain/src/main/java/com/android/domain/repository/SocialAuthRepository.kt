package com.android.domain.repository

import com.android.domain.entity.AuthEntity
import com.android.domain.util.ApiResult

interface SocialAuthRepository {
    suspend fun doLogin(sub: String, fcmToken: String): ApiResult<AuthEntity>
    suspend fun doRestore(): ApiResult<Unit>
}