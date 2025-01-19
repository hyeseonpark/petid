package com.petid.domain.repository

import com.petid.domain.entity.CommonInfo
import com.petid.domain.entity.ContentEntity
import com.petid.domain.entity.ContentLikeEntity
import com.petid.domain.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface BlogMainRepository {
    suspend fun getContentList(category: String): ApiResult<List<ContentEntity>>
    suspend fun getCommonInfoList(category: String): ApiResult<List<CommonInfo>>
    suspend fun getContentImage(filePath: String): String
    suspend fun doContentLike(contentId: Int): ApiResult<ContentLikeEntity>
    suspend fun cancelContentLike(contentId: Int): ApiResult<ContentLikeEntity>
}