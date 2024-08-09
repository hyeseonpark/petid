package com.android.data.repository.login

import com.android.domain.entity.LoginEntity
import com.android.domain.util.ApiResult

interface LoginDataSource {
    interface Remote {
        suspend fun getLogin(sub: String, fcmToken: String): ApiResult<LoginEntity>
    }
}