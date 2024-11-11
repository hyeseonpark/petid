package com.android.domain.repository

import com.android.domain.entity.ContentEntity
import com.android.domain.entity.ContentLikeEntity
import com.android.domain.util.ApiResult

interface BlogMainRepository {
    suspend fun getContentList(category: String): ApiResult<List<ContentEntity>>
    suspend fun getContentImage(filePath: String): String
    suspend fun doContentLike(contentId: Int): ApiResult<ContentLikeEntity>
}