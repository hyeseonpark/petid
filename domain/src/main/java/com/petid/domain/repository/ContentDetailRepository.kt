package com.petid.domain.repository

import com.petid.domain.entity.ContentEntity
import com.petid.domain.util.ApiResult

interface ContentDetailRepository {
    suspend fun getContentDetail(contentId: Int): ApiResult<ContentEntity>
}