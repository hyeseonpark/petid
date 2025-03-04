package com.petid.data.source.remote

import com.petid.data.dto.response.ContentLikeResponse
import com.petid.data.dto.response.ContentResponse

interface BlogMainRemoteDataSource {
    suspend fun getContentList(category: String): List<ContentResponse>
    suspend fun doContentLike(contentId: Int): ContentLikeResponse
    suspend fun cancelContentLike(contentId: Int): ContentLikeResponse
}