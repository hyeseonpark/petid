package com.petid.data.repository.remote

import com.petid.data.dto.response.toDomain
import com.petid.data.source.remote.MyInfoRemoteDataSource
import com.petid.data.util.mapApiResult
import com.petid.domain.entity.MemberInfoEntity
import com.petid.domain.entity.UpdateMemberInfoEntity
import com.petid.domain.repository.MyInfoRepository
import com.petid.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyInfoRepositoryImpl @Inject constructor(
    private val myInfoRemoteDataSource: MyInfoRemoteDataSource,
): MyInfoRepository {
    override suspend fun getMemberInfo(): ApiResult<MemberInfoEntity> =
        runCatching {
            myInfoRemoteDataSource.getMemberInfo().toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getProfileImageUrl(imagePath: String): ApiResult<String> =
        runCatching {
            myInfoRemoteDataSource.getProfileImageUrl(imagePath)
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun updateMemberInfo(
        address: String, addressDetail: String, phone: String
    ): ApiResult<UpdateMemberInfoEntity> =
        runCatching {
            myInfoRemoteDataSource.updateMemberInfo(address, addressDetail, phone).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun updateMemberPhoto(filePath: String): ApiResult<String> =
        runCatching {
            myInfoRemoteDataSource.updateMemberPhoto(filePath)
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun doWithdraw(): ApiResult<Unit> =
        runCatching {
            myInfoRemoteDataSource.doWithdraw()
        }.mapApiResult { ApiResult.Success(it) }
}