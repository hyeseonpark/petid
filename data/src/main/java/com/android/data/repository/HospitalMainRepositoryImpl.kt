package com.android.data.repository

import com.android.data.api.HosptialAPI
import com.android.data.dto.response.ErrorResponse
import com.android.data.dto.response.toDomain
import com.android.data.source.remote.HospitalMainRemoteDataSource
import com.android.domain.entity.HospitalEntity
import com.android.domain.entity.LocationEntity
import com.android.domain.repository.HospitalMainRepository
import com.android.domain.util.ApiResult
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
        eupmundongId: Int
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
        eupmundongId: Int,
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