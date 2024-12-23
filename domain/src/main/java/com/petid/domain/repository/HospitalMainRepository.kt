package com.petid.domain.repository

import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.LocationEntity
import com.petid.domain.util.ApiResult

interface HospitalMainRepository {
    suspend fun getSido(): ApiResult<List<LocationEntity>>
    suspend fun getSigunguList(id: Int): ApiResult<List<LocationEntity>>
    suspend fun getEupmundongList(id: Int): ApiResult<List<LocationEntity>>

    suspend fun getHospitalList(sidoId: Int, sigunguId: Int, eupmundongId: Int): ApiResult<List<HospitalEntity>>
    suspend fun getHospitalListLoc(sidoId: Int, sigunguId: Int, eupmundongId: Int,
                                   lat: Double, lon: Double): ApiResult<List<HospitalEntity>>

    suspend fun getHospitalImageUrl(filePath: String): String
}