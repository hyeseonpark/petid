package com.petid.domain.repository

import com.petid.domain.entity.BannerEntity
import com.petid.domain.util.ApiResult

interface HomeMainRepository {
    suspend fun getBannerList(type: String): ApiResult<List<BannerEntity>>
    suspend fun getBannerImage(imagePath: String): ApiResult<String>
}