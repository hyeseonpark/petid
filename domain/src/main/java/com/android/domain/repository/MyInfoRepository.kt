package com.android.domain.repository

import com.android.domain.entity.MemberInfoEntity
import com.android.domain.entity.UpdateMemberInfoEntity
import com.android.domain.util.ApiResult

interface MyInfoRepository {
    suspend fun getMemberInfo(): ApiResult<MemberInfoEntity>
    suspend fun uploadProfileImage(imagePath: String): String
    suspend fun getProfileImageUrl(imagePath: String): String
    suspend fun updateMemberInfo(): ApiResult<UpdateMemberInfoEntity>
}