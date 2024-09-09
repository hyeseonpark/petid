package com.android.domain.repository

import com.android.domain.entity.HospitalEntity
import com.android.domain.entity.LocationEntity
import com.android.domain.util.ApiResult

interface HospitalMainRepository {
    suspend fun getSido(): ApiResult<List<LocationEntity>>
    suspend fun getSigunguList(id: Int): ApiResult<List<LocationEntity>>
    suspend fun getEupmundongList(id: Int): ApiResult<List<LocationEntity>>

    suspend fun getHospitalList(sidoId: Int, sigunguId: Int, eupmundongId: Int): ApiResult<List<HospitalEntity>>
}