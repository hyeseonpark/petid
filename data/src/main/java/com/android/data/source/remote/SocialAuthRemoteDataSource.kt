package com.android.data.source.remote

import com.android.domain.entity.AuthEntity
import com.android.domain.util.ApiResult

interface SocialAuthRemoteDataSource {
        suspend fun getLogin(sub: String, fcmToken: String): ApiResult<AuthEntity>
        suspend fun doRestore(): ApiResult<Unit>
}