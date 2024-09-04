package com.android.domain.usecase.login

import com.android.domain.entity.AuthEntity
import com.android.domain.repository.TermsRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject

class DoJoinUseCase @Inject constructor(
    private val termsRepository: TermsRepository
){
    suspend operator fun invoke(platform: String, sub: String, fcmToken: String, ad: Boolean)
    : ApiResult<AuthEntity> {
        return termsRepository.doJoin(platform,sub, fcmToken, ad)
    }
}