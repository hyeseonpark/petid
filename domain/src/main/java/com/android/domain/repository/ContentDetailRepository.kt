package com.android.domain.repository

import com.android.domain.entity.ContentEntity
import com.android.domain.util.ApiResult

interface ContentDetailRepository {
    suspend fun getContentDetail(contentId: Int): ApiResult<ContentEntity>
}