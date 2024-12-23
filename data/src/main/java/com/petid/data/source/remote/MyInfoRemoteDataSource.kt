package com.petid.data.source.remote

import com.petid.domain.entity.MemberInfoEntity
import com.petid.domain.entity.UpdateMemberInfoEntity
import com.petid.domain.util.ApiResult

interface MyInfoRemoteDataSource {
    suspend fun getMemberInfo(): ApiResult<MemberInfoEntity>
    suspend fun getProfileImageUrl(imagePath: String): ApiResult<String>
    suspend fun updateMemberInfo(
        address: String,
        addressDetail: String,
        phone: String
    ): ApiResult<UpdateMemberInfoEntity>
    suspend fun updateMemberPhoto(filePath: String): ApiResult<String>
    suspend fun doWithdraw(): ApiResult<Unit>
}