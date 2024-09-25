package com.android.data.source.remote

import com.android.domain.entity.BannerEntity
import com.android.domain.util.ApiResult

interface HomeMainRemoteDataSource {
    suspend fun getBannerList(type: String): ApiResult<List<BannerEntity>>
    suspend fun getBannerImage(imagePath: String): ApiResult<String>
}