package com.petid.data.repository.remote

import com.petid.data.api.HosptialAPI
import com.petid.data.source.remote.HospitalMainRemoteDataSource
import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.LocationEntity
import com.petid.domain.repository.HospitalMainRepository
import com.petid.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HospitalMainRepositoryImpl @Inject constructor(
    private val remoteDataSource: HospitalMainRemoteDataSource,
    private val hospitalAPI: HosptialAPI
) : HospitalMainRepository{
    override suspend fun getSido(): ApiResult<List<LocationEntity>> =
        remoteDataSource.getSido()

    override suspend fun getSigunguList(id: Int): ApiResult<List<LocationEntity>> =
        remoteDataSource.getSigunguList(id)

    override suspend fun getEupmundongList(id: Int): ApiResult<List<LocationEntity>> =
        remoteDataSource.getEupmundongList(id)

    override suspend fun getHospitalList(
        sidoId: Int,
        sigunguId: Int,
        eupmundongId: Int?
    ): ApiResult<List<HospitalEntity>> =
        remoteDataSource.getHospitalList(sidoId, sigunguId, eupmundongId)

    override suspend fun getHospitalListLoc(
        sidoId: Int,
        sigunguId: Int,
        eupmundongId: Int?,
        lat: Double,
        lon: Double
    ): ApiResult<List<HospitalEntity>> =
        remoteDataSource.getHospitalListLoc(sidoId, sigunguId, eupmundongId, lat, lon)

    override suspend fun getHospitalImageUrl(filePath: String): String {
        return try {
            hospitalAPI.getHospitalImageUrl(filePath)
        } catch (e: Exception) {
            ""
        }
    }

}