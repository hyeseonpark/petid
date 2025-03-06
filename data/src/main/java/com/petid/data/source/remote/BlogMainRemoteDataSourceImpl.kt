package com.petid.data.source.remote

import com.petid.data.api.ContentAPI
import com.petid.data.dto.response.ContentLikeResponse
import com.petid.data.dto.response.ContentResponse
import javax.inject.Inject

class BlogMainRemoteDataSourceImpl @Inject constructor(
    private val contentAPI: ContentAPI
): BlogMainRemoteDataSource {
    override suspend fun getContentList(category: String): List<ContentResponse> =
        contentAPI.getContentList(category)

    override suspend fun doContentLike(contentId: Int): ContentLikeResponse =
        contentAPI.doContentLike(contentId)

    override suspend fun cancelContentLike(contentId: Int): ContentLikeResponse =
        contentAPI.cancelContentLike(contentId)
}