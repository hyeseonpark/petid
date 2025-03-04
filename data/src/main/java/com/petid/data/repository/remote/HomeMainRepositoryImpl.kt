package com.petid.data.repository.remote

import com.petid.data.dto.response.toDomain
import com.petid.data.source.remote.HomeMainRemoteDataSource
import com.petid.data.util.mapApiResult
import com.petid.domain.entity.BannerEntity
import com.petid.domain.repository.HomeMainRepository
import com.petid.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeMainRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeMainRemoteDataSource,
) : HomeMainRepository{
    override suspend fun getBannerList(type: String): ApiResult<List<BannerEntity>> =
        runCatching {
            remoteDataSource.getBannerList(type).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getBannerImage(imagePath: String): ApiResult<String> =
        runCatching {
            remoteDataSource.getBannerImage(imagePath)
        }.mapApiResult { ApiResult.Success(it) }
}