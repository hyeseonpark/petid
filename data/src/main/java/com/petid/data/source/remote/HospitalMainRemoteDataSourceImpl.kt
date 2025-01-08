package com.petid.data.source.remote

import com.petid.data.api.HosptialAPI
import com.petid.data.api.LocationAPI
import com.petid.data.dto.response.toDomain
import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.LocationEntity
import com.petid.domain.util.ApiResult
import com.petid.data.util.mapApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HospitalMainRemoteDataSourceImpl @Inject constructor(
    private val locationAPI: LocationAPI,
    private val hospitalAPI: HosptialAPI
) : HospitalMainRemoteDataSource {
    override suspend fun getSido(): ApiResult<List<LocationEntity>> {
        return runCatching {
            locationAPI.getSidoList().toDomain()
        }.mapApiResult { ApiResult.Success(it) }
    }

    override suspend fun getSigunguList(id: Int): ApiResult<List<LocationEntity>> {
        return runCatching {
            locationAPI.getSigunguList(id).toDomain()
        }.mapApiResult { ApiResult.Success(it) }
    }

    override suspend fun getEupmundongList(id: Int): ApiResult<List<LocationEntity>> {
        return runCatching {
            locationAPI.getEupmyeondongList(id).toDomain()
        }.mapApiResult { ApiResult.Success(it) }
    }

    override suspend fun getHospitalList(
        sidoId: Int,
        sigunguId: Int,
        eupmundongId: Int?
    ): ApiResult<List<HospitalEntity>> {
        return runCatching {
            hospitalAPI.getHospitalList(sidoId, sigunguId, nullToEmpty(eupmundongId)).toDomain()
        }.mapApiResult { ApiResult.Success(it) }
    }

    override suspend fun getHospitalListLoc(
        sidoId: Int,
        sigunguId: Int,
        eupmundongId: Int?,
        lat: Double,
        lon: Double
    ): ApiResult<List<HospitalEntity>> {
        return runCatching {
            hospitalAPI.getHospitalListByLocation(
                sidoId, sigunguId, nullToEmpty(eupmundongId), lat, lon).toDomain()
        }.mapApiResult { ApiResult.Success(it) }
    }

    /**
     * api 규격에 맞추기 위한 Int to String 변환
     */
    private fun nullToEmpty(value: Int?): String {
        if(value == -1) return ""
        return value?.toString() ?: ""
    }
}