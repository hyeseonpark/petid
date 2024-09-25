package com.android.domain.repository

import com.android.domain.entity.BannerEntity
import com.android.domain.util.ApiResult

interface HomeMainRepository {
    suspend fun getBannerList(type: String): ApiResult<List<BannerEntity>>
    suspend fun getBannerImage(imagePath: String): ApiResult<String>
}