package com.android.data.source.remote

import com.android.domain.entity.ContentEntity
import com.android.domain.entity.ContentLikeEntity
import com.android.domain.util.ApiResult

interface BlogMainRemoteDataSource {
    suspend fun getContentList(category: String): ApiResult<List<ContentEntity>>
    suspend fun doContentLike(contentId: Int): ApiResult<ContentLikeEntity>
}