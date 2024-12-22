package com.android.data.repository

import com.android.data.api.MemberAPI
import com.android.data.source.remote.MyInfoRemoteDataSource
import com.android.domain.entity.MemberInfoEntity
import com.android.domain.entity.UpdateMemberInfoEntity
import com.android.domain.repository.MyInfoRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyInfoRepositoryImpl @Inject constructor(
    private val memberAPI: MemberAPI,
    private val myInfoRemoteDataSource: MyInfoRemoteDataSource,
): MyInfoRepository {
    override suspend fun getMemberInfo(): ApiResult<MemberInfoEntity> {
        return when (val result = myInfoRemoteDataSource.getMemberInfo()) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun uploadProfileImage(imagePath: String): String {
        return try {
            memberAPI.uploadProfileImage(imagePath)
        } catch (e: Exception) {
            ""
        }
    }

    override suspend fun getProfileImageUrl(imagePath: String): ApiResult<String> {
        return when (val result = myInfoRemoteDataSource.getProfileImageUrl(imagePath)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun updateMemberInfo(
        address: String, addressDetail: String, phone: String
    ): ApiResult<UpdateMemberInfoEntity> {
        return when (val result =
            myInfoRemoteDataSource.updateMemberInfo(address, addressDetail, phone)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun updateMemberPhoto(filePath: String): ApiResult<String> {
        return when (val result = myInfoRemoteDataSource.updateMemberPhoto(filePath)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun doWithdraw(): ApiResult<Unit> {
        return when (val result = myInfoRemoteDataSource.doWithdraw()) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }
}