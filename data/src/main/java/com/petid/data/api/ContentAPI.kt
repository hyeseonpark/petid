package com.petid.data.api

import com.petid.data.dto.response.ContentLikeResponse
import com.petid.data.dto.response.ContentResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ContentAPI {

    /**
     * 7.1 카테고리 별 컨텐츠 조회
     */
    @GET("/v1/content")
    suspend fun getContentList(
        @Query("category") category: String
    ): List<ContentResponse>

    /**
     * 7.2 컨텐츠 조회
     */
    @GET("/v1/content/{contentId}")
    suspend fun getContentDetail(
        @Path("contentId") contentId: Int
    ): ContentResponse

    /**
     * 7.3 컨텐츠 좋아요
     */
    @POST("/v1/content/{contentId}/like")
    suspend fun doContentLike(
        @Path("contentId") contentId: Int
    ): ContentLikeResponse

    /**
     * 7.4 컨텐츠 좋아요 취소
     */
    @DELETE("/v1/content/{contentId}/like")
    suspend fun cancelContentLike(
        @Path("contentId") contentId: Int
    ): ContentLikeResponse

    /**
     * 7.5 컨텐츠 이미지 조회
     */
    @GET("/v1/content/presigned-get-url")
    suspend fun getContentImage(
        @Query("filePath") filePath: String
    ): String
}