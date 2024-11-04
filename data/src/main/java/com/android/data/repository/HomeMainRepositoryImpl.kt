package com.android.data.repository

import com.android.data.api.BannerAPI
import com.android.data.source.remote.HomeMainRemoteDataSource
import com.android.domain.entity.BannerEntity
import com.android.domain.repository.HomeMainRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeMainRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeMainRemoteDataSource,
    private val bannerAPI: BannerAPI,
) : HomeMainRepository{
    override suspend fun getBannerList(type: String): ApiResult<List<BannerEntity>> {
        return when (val result = remoteDataSource.getBannerList(type)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    /*override suspend fun getBannerImage(imagePath: String): ApiResult<String> {
        return when (val result = remoteDataSource.getBannerImage(imagePath)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }*/
    override suspend fun getBannerImage(imagePath: String): String {
//        return try {
//            val response = bannerAPI.getBannerImage(imagePath)
//            ApiResult.Success(response)
//        } catch (e:Exception) {
//            ApiResult.Error(e.message ?: "Unknown Error")
//        }
        return bannerAPI.getBannerImage(imagePath)
    }

}