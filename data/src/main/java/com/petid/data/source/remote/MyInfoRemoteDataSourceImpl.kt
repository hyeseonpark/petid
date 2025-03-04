package com.petid.data.source.remote

import com.petid.data.api.MemberAPI
import com.petid.data.dto.request.UpdateMemberInfoRequest
import com.petid.data.dto.response.MemberInfoResponse
import com.petid.data.dto.response.UpdateMemberInfoResponse
import javax.inject.Inject

class MyInfoRemoteDataSourceImpl @Inject constructor(
    private val memberAPI: MemberAPI,
): MyInfoRemoteDataSource {
    override suspend fun getMemberInfo(): MemberInfoResponse =
        memberAPI.getMemberInfo()

    override suspend fun getProfileImageUrl(imagePath: String): String =
        memberAPI.getProfileImageUrl(imagePath)

    override suspend fun updateMemberInfo(
        address: String, addressDetail: String, phone: String
    ): UpdateMemberInfoResponse =
        memberAPI.updateMemberInfo(
            UpdateMemberInfoRequest(address, addressDetail, phone)
        )

    override suspend fun updateMemberPhoto(filePath: String): String =
        memberAPI.updateMemberPhoto(filePath)

    override suspend fun doWithdraw(): Unit =
        memberAPI.doWithdraw()
}