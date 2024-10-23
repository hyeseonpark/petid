package com.android.data.api

import com.android.data.dto.response.ContentResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ContentAPI {
    @GET("/v1/content")
    suspend fun getContentList(
        @Query("category") category: String
    ): List<ContentResponse>

    @GET("/v1/content/{contentId}")
    suspend fun getContentDetail(
        @Path("contentId") contentId: Int
    ): ContentResponse

    @POST("/v1/content/{contentId}/like")
    suspend fun doContentLike(
        @Path("contentId") contentId: Int
    )
}