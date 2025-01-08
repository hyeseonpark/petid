package com.petid.data.source.remote

import com.petid.data.api.MemberAPI
import com.petid.data.dto.request.UpdateMemberInfoRequest
import com.petid.data.dto.response.ErrorResponse
import com.petid.data.dto.response.toDomain
import com.petid.domain.entity.MemberInfoEntity
import com.petid.domain.entity.UpdateMemberInfoEntity
import com.petid.domain.util.ApiResult
import com.google.gson.Gson
import com.petid.data.util.mapApiResult
import retrofit2.HttpException
import javax.inject.Inject

class MyInfoRemoteDataSourceImpl @Inject constructor(
    private val memberAPI: MemberAPI,
): MyInfoRemoteDataSource {
    override suspend fun getMemberInfo(): ApiResult<MemberInfoEntity> =
        runCatching {
            memberAPI.getMemberInfo().toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getProfileImageUrl(imagePath: String): ApiResult<String> =
        runCatching {
            memberAPI.getProfileImageUrl(imagePath)
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun updateMemberInfo(
        address: String, addressDetail: String, phone: String
    ): ApiResult<UpdateMemberInfoEntity> =
        runCatching {
            memberAPI.updateMemberInfo(
                UpdateMemberInfoRequest(address, addressDetail, phone)
            ).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun updateMemberPhoto(filePath: String): ApiResult<String> =
        runCatching {
            memberAPI.updateMemberPhoto(filePath)
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun doWithdraw(): ApiResult<Unit> =
        runCatching {
            memberAPI.doWithdraw()
        }.mapApiResult { ApiResult.Success(it) }
}