package com.petid.data.source.remote

import com.petid.data.api.BannerAPI
import com.petid.data.dto.response.BannerResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeMainRemoteDataSourceImpl @Inject constructor(
    private val bannerAPI: BannerAPI,
) : HomeMainRemoteDataSource{
    override suspend fun getBannerList(type: String): List<BannerResponse> =
        bannerAPI.getBannerList(type)

    override suspend fun getBannerImage(imagePath: String): String =
        bannerAPI.getBannerImage(imagePath)
}
