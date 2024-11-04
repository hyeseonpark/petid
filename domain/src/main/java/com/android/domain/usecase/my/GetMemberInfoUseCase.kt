package com.android.domain.usecase.my

import com.android.domain.entity.MemberInfoEntity
import com.android.domain.repository.MyInfoRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject

class GetMemberInfoUseCase @Inject constructor(
    private val myInfoRepository: MyInfoRepository
) {
    suspend operator fun invoke(): ApiResult<MemberInfoEntity> {
        return myInfoRepository.getMemberInfo()
    }
}