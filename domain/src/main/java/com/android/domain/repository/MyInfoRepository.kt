package com.android.domain.repository

import com.android.domain.entity.MemberInfoEntity
import com.android.domain.entity.UpdateMemberInfoEntity
import com.android.domain.util.ApiResult

interface MyInfoRepository {
    suspend fun getMemberInfo(): ApiResult<MemberInfoEntity>
    suspend fun uploadProfileImage(imagePath: String): String
    suspend fun getProfileImageUrl(imagePath: String): String
    suspend fun updateMemberInfo(
        name: String,
        address: String,
        addressDetail: String,
        phone: String
    ): ApiResult<UpdateMemberInfoEntity>
    suspend fun updateMemberPhoto(filePath: String): ApiResult<String>
}