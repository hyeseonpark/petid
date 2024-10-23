package com.android.data.source.remote

import com.android.domain.entity.ContentEntity
import com.android.domain.util.ApiResult

interface ContentDetailRemoteDataSource {
    suspend fun getContentDetail(contentId: Int): ApiResult<ContentEntity>
    // suspend fun getHospitalListByLocation()
}