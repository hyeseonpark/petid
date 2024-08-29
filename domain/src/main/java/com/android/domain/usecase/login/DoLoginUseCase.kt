package com.android.domain.usecase.login

import com.android.domain.entity.AuthEntity
import com.android.domain.repository.SocialAuthRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject

class DoLoginUseCase @Inject constructor(
    private val socialAuthRepository : SocialAuthRepository
) {
    suspend operator fun invoke(sub: String, fcmToken: String): ApiResult<AuthEntity> {
        return socialAuthRepository.doLogin(sub, fcmToken)
    }

}