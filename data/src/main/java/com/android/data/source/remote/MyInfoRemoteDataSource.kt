package com.android.data.source.remote

import com.android.domain.entity.MemberInfoEntity
import com.android.domain.entity.UpdateMemberInfoEntity
import com.android.domain.util.ApiResult

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