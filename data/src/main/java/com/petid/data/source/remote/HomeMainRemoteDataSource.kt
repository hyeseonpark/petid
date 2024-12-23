package com.petid.data.source.remote

import com.petid.domain.entity.BannerEntity
import com.petid.domain.util.ApiResult

interface HomeMainRemoteDataSource {
    suspend fun getBannerList(type: String): ApiResult<List<BannerEntity>>
    suspend fun getBannerImage(imagePath: String): ApiResult<String>
}