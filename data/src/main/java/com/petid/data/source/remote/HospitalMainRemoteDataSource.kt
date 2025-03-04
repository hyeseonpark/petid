package com.petid.data.source.remote

import com.petid.data.dto.response.HospitalResponse
import com.petid.data.dto.response.LocationResponse
import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.LocationEntity
import com.petid.domain.util.ApiResult

interface HospitalMainRemoteDataSource {
    suspend fun getSido(): List<LocationResponse>
    suspend fun getSigunguList(id: Int): List<LocationResponse>
    suspend fun getEupmundongList(id: Int): List<LocationResponse>

    suspend fun getHospitalList(sidoId: Int, sigunguId: Int, eupmundongId: Int?): List<HospitalResponse>
    suspend fun getHospitalListLoc(sidoId: Int, sigunguId: Int, eupmundongId: Int?,
                                   lat: Double, lon: Double): List<HospitalResponse>
}