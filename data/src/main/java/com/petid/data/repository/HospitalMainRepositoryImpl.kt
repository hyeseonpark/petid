package com.petid.data.repository

import com.petid.data.api.HosptialAPI
import com.petid.data.dto.response.ErrorResponse
import com.petid.data.dto.response.toDomain
import com.petid.data.source.remote.HospitalMainRemoteDataSource
import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.LocationEntity
import com.petid.domain.repository.HospitalMainRepository
import com.petid.domain.util.ApiResult
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HospitalMainRepositoryImpl @Inject constructor(
    private val remoteDataSource: HospitalMainRemoteDataSource,
    private val hospitalAPI: HosptialAPI
) : HospitalMainRepository{
    override suspend fun getSido(): ApiResult<List<LocationEntity>> {
        return when (val result = remoteDataSource.getSido()) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun getSigunguList(id: Int): ApiResult<List<LocationEntity>> {
        return when (val result = remoteDataSource.getSigunguList(id)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun getEupmundongList(id: Int): ApiResult<List<LocationEntity>> {
        return when (val result = remoteDataSource.getEupmundongList(id)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun getHospitalList(
        sidoId: Int,
        sigunguId: Int,
        eupmundongId: Int?
    ): ApiResult<List<HospitalEntity>> {
        return when (val result = remoteDataSource.getHospitalList(sidoId, sigunguId, eupmundongId)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun getHospitalListLoc(
        sidoId: Int,
        sigunguId: Int,
        eupmundongId: Int?,
        lat: Double,
        lon: Double
    ): ApiResult<List<HospitalEntity>> {
        return when (val result = remoteDataSource.getHospitalListLoc(
            sidoId, sigunguId, eupmundongId, lat, lon)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun getHospitalImageUrl(filePath: String): String {
        return try {
            hospitalAPI.getHospitalImageUrl(filePath)
        } catch (e: Exception) {
            ""
        }
    }

}