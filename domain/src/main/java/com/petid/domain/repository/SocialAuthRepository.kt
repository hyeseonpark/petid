package com.petid.domain.repository

import com.petid.domain.entity.AuthEntity
import com.petid.domain.util.ApiResult

interface SocialAuthRepository {
    suspend fun doLogin(sub: String, fcmToken: String): ApiResult<AuthEntity>
    suspend fun doRestore(): ApiResult<Unit>
}