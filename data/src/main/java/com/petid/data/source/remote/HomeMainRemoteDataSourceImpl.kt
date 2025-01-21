package com.petid.data.source.remote

import com.petid.data.api.BannerAPI
import com.petid.data.dto.request.PresignedGetUrlRequest
import com.petid.data.dto.response.ErrorResponse
import com.petid.data.dto.response.toDomain
import com.petid.domain.entity.BannerEntity
import com.petid.domain.util.ApiResult
import com.google.gson.Gson
import com.petid.data.util.mapApiResult
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeMainRemoteDataSourceImpl @Inject constructor(
    private val bannerAPI: BannerAPI,
) : HomeMainRemoteDataSource{
    override suspend fun getBannerList(type: String): ApiResult<List<BannerEntity>> =
        runCatching {
            bannerAPI.getBannerList(type).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getBannerImage(imagePath: String): ApiResult<String> =
        runCatching {
            bannerAPI.getBannerImage(imagePath)
        }.mapApiResult { ApiResult.Success(it) }

}