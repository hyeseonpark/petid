package com.android.domain.usecase.login

import com.android.domain.entity.LoginEntity
import com.android.domain.repository.LoginRepository
import com.android.domain.util.ApiResult

class GetLogin(
    private val loginRepository : LoginRepository
) {
    suspend operator fun invoke(sub: String, fcmToken: String): ApiResult<LoginEntity> =
        loginRepository.getLogin(sub, fcmToken)
}