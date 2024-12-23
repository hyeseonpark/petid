package com.petid.data.source.remote

import com.petid.domain.entity.AuthEntity
import com.petid.domain.util.ApiResult

interface SocialAuthRemoteDataSource {
        suspend fun getLogin(sub: String, fcmToken: String): ApiResult<AuthEntity>
        suspend fun doRestore(): ApiResult<Unit>
}