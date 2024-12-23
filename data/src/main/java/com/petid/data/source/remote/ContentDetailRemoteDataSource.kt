package com.petid.data.source.remote

import com.petid.domain.entity.ContentEntity
import com.petid.domain.util.ApiResult

interface ContentDetailRemoteDataSource {
    suspend fun getContentDetail(contentId: Int): ApiResult<ContentEntity>
    // suspend fun getHospitalListByLocation()
}