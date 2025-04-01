package com.petid.data.repository.remote

import com.petid.data.api.HospitalAPI
import com.petid.data.dto.response.toDomain
import com.petid.data.source.remote.HospitalMainRemoteDataSource
import com.petid.data.util.mapApiResult
import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.LocationEntity
import com.petid.domain.repository.HospitalMainRepository
import com.petid.domain.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HospitalMainRepositoryImpl @Inject constructor(
    private val remoteDataSource: HospitalMainRemoteDataSource,
    private val hospitalAPI: HospitalAPI
) : HospitalMainRepository{
    override suspend fun getSido(): ApiResult<List<LocationEntity>> =
        runCatching {
            remoteDataSource.getSido().toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getSigunguList(id: Int): ApiResult<List<LocationEntity>> =
        runCatching {
            remoteDataSource.getSigunguList(id).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getEupmundongList(id: Int): ApiResult<List<LocationEntity>> =
        runCatching {
            remoteDataSource.getEupmundongList(id).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getHospitalList(
        sidoId: Int,
        sigunguId: Int,
        eupmundongId: Int?
    ): ApiResult<List<HospitalEntity>> =
        runCatching {
            remoteDataSource.getHospitalList(sidoId, sigunguId, eupmundongId).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getHospitalListLoc(
        sidoId: Int,
        sigunguId: Int,
        eupmundongId: Int?,
        lat: Double,
        lon: Double
    ): ApiResult<List<HospitalEntity>> =
        runCatching {
            remoteDataSource.getHospitalListLoc(sidoId, sigunguId, eupmundongId, lat, lon).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getHospitalImageUrl(filePath: String): Flow<String> =
        flow {
            val res = hospitalAPI.getHospitalImageUrl(filePath)
            emit(res)
        }.flowOn(Dispatchers.IO)

}