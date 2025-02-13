package com.petid.data.repository.remote

import com.petid.data.api.MemberAPI
import com.petid.data.source.remote.MyInfoRemoteDataSource
import com.petid.domain.entity.MemberInfoEntity
import com.petid.domain.entity.UpdateMemberInfoEntity
import com.petid.domain.repository.MyInfoRepository
import com.petid.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyInfoRepositoryImpl @Inject constructor(
    private val memberAPI: MemberAPI,
    private val myInfoRemoteDataSource: MyInfoRemoteDataSource,
): MyInfoRepository {
    override suspend fun getMemberInfo(): ApiResult<MemberInfoEntity> =
        myInfoRemoteDataSource.getMemberInfo()

    override suspend fun getProfileImageUrl(imagePath: String): ApiResult<String> =
        myInfoRemoteDataSource.getProfileImageUrl(imagePath)

    override suspend fun updateMemberInfo(
        address: String, addressDetail: String, phone: String
    ): ApiResult<UpdateMemberInfoEntity> =
        myInfoRemoteDataSource.updateMemberInfo(address, addressDetail, phone)

    override suspend fun updateMemberPhoto(filePath: String): ApiResult<String> =
        myInfoRemoteDataSource.updateMemberPhoto(filePath)

    override suspend fun doWithdraw(): ApiResult<Unit> =
        myInfoRemoteDataSource.doWithdraw()
}