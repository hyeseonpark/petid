package com.android.domain.repository

import com.android.domain.entity.LoginEntity
import com.android.domain.util.ApiResult

interface LoginRepository {
    suspend fun getLogin(sub: String, fcmToken: String): ApiResult<LoginEntity>
}