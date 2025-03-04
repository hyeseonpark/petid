package com.petid.data.source.remote

import com.petid.data.dto.response.BannerResponse

interface HomeMainRemoteDataSource {
    suspend fun getBannerList(type: String): List<BannerResponse>
    suspend fun getBannerImage(imagePath: String): String
}