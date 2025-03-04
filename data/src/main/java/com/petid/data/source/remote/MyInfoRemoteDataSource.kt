package com.petid.data.source.remote

import com.petid.data.dto.response.MemberInfoResponse
import com.petid.data.dto.response.UpdateMemberInfoResponse

interface MyInfoRemoteDataSource {
    suspend fun getMemberInfo(): MemberInfoResponse
    suspend fun getProfileImageUrl(imagePath: String): String
    suspend fun updateMemberInfo(
        address: String,
        addressDetail: String,
        phone: String
    ): UpdateMemberInfoResponse
    suspend fun updateMemberPhoto(filePath: String): String
    suspend fun doWithdraw(): Unit
}