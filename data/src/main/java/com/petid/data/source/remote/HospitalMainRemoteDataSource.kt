package com.petid.data.source.remote

import com.petid.data.dto.response.HospitalResponse
import com.petid.data.dto.response.LocationResponse

interface HospitalMainRemoteDataSource {
    suspend fun getSido(): List<LocationResponse>
    suspend fun getSigunguList(id: Int): List<LocationResponse>
    suspend fun getEupmundongList(id: Int): List<LocationResponse>

    suspend fun getHospitalList(sidoId: Int, sigunguId: Int, eupmundongId: Int?): List<HospitalResponse>
    suspend fun getHospitalListLoc(sidoId: Int, sigunguId: Int, eupmundongId: Int?,
                                   lat: Double, lon: Double): List<HospitalResponse>
}