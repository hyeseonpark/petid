package com.android.data.repository

import com.android.data.source.remote.HomeMainRemoteDataSource
import com.android.domain.entity.BannerEntity
import com.android.domain.repository.HomeMainRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeMainRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeMainRemoteDataSource,
) : HomeMainRepository{
    override suspend fun getBannerList(type: String): ApiResult<List<BannerEntity>> {
        return when (val result = remoteDataSource.getBannerList(type)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun getBannerImage(imagePath: String): ApiResult<String> {
        return when (val result = remoteDataSource.getBannerImage(imagePath)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

}