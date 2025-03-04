package com.petid.data.source.remote

import com.petid.data.api.HospitalAPI
import com.petid.data.api.LocationAPI
import com.petid.data.dto.response.HospitalResponse
import com.petid.data.dto.response.LocationResponse
import com.petid.data.dto.response.toDomain
import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.LocationEntity
import com.petid.domain.util.ApiResult
import com.petid.data.util.mapApiResult
import com.petid.data.util.nullToEmpty
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HospitalMainRemoteDataSourceImpl @Inject constructor(
    private val locationAPI: LocationAPI,
    private val hospitalAPI: HospitalAPI
) : HospitalMainRemoteDataSource {
    override suspend fun getSido(): List<LocationResponse> =
        locationAPI.getSidoList()

    override suspend fun getSigunguList(id: Int): List<LocationResponse> =
        locationAPI.getSigunguList(id)

    override suspend fun getEupmundongList(id: Int): List<LocationResponse> =
        locationAPI.getEupmyeondongList(id)

    override suspend fun getHospitalList(
        sidoId: Int,
        sigunguId: Int,
        eupmundongId: Int?
    ): List<HospitalResponse> =
        hospitalAPI.getHospitalList(sidoId, sigunguId, eupmundongId.nullToEmpty())

    override suspend fun getHospitalListLoc(
        sidoId: Int,
        sigunguId: Int,
        eupmundongId: Int?,
        lat: Double,
        lon: Double
    ): List<HospitalResponse> =
        hospitalAPI.getHospitalListByLocation(
            sidoId, sigunguId, eupmundongId.nullToEmpty(), lat, lon)

}