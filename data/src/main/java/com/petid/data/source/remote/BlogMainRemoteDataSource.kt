package com.petid.data.source.remote

import com.petid.domain.entity.ContentEntity
import com.petid.domain.entity.ContentLikeEntity
import com.petid.domain.util.ApiResult

interface BlogMainRemoteDataSource {
    suspend fun getContentList(category: String): ApiResult<List<ContentEntity>>
    suspend fun doContentLike(contentId: Int): ApiResult<ContentLikeEntity>
    suspend fun cancelContentLike(contentId: Int): ApiResult<ContentLikeEntity>
}