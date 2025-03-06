package com.petid.domain.repository

import com.petid.domain.entity.AuthEntity
import com.petid.domain.util.ApiResult

interface TermsRepository {
    suspend fun doJoin(platform: String, token: String, fcmToken: String, ad: Boolean): ApiResult<AuthEntity>
}