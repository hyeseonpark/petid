package com.petid.data.repository.remote

import com.petid.data.api.BannerAPI
import com.petid.data.source.remote.HomeMainRemoteDataSource
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
        remoteDataSource.getBannerList(type)

    override suspend fun getBannerImage(imagePath: String): ApiResult<String> =
        remoteDataSource.getBannerImage(imagePath)
}