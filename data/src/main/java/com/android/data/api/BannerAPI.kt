package com.android.data.api

import com.android.data.dto.request.PresignedGetUrlRequest
import com.android.data.dto.response.BannerResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BannerAPI {

    /**
     * 배너 조회
     *
     * @param type 배너타입
     */
    @GET("/v1/banner/type")
    suspend fun getBannerList(
        @Query("type") type: String
    ): List<BannerResponse>

    @GET("/v1/banner/presigned-get-url")
    suspend fun getBannerImage(
        @Query("imagePath") imagePath: String
    ): String
}