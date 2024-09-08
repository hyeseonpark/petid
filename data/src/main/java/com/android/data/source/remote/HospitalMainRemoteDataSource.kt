package com.android.data.source.remote

import com.android.domain.entity.LocationEntity
import com.android.domain.util.ApiResult

interface HospitalMainRemoteDataSource {
    suspend fun getSido(): ApiResult<List<LocationEntity>>
    suspend fun getSigunguList(id: Int): ApiResult<List<LocationEntity>>
}