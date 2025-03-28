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

    /**
         * Retrieves a list of hospitals based on region identifiers and geographic coordinates.
         *
         * This method fetches hospital data by filtering with the provided region IDs (sido, sigungu, and an optional eupmundong)
         * along with latitude and longitude values. It encapsulates the result within an ApiResult, returning a successful result
         * if the data is retrieved and converted correctly.
         *
         * @param sidoId the identifier for the primary region.
         * @param sigunguId the identifier for the secondary region.
         * @param eupmundongId an optional identifier for the tertiary region.
         * @param lat the latitude coordinate used for proximity filtering.
         * @param lon the longitude coordinate used for proximity filtering.
         * @return an ApiResult containing a list of HospitalEntity instances on success.
         */
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

    /**
         * Retrieves the hospital image URL asynchronously.
         *
         * This method fetches the URL corresponding to the provided hospital image file path,
         * returning the result as a [Flow] that emits the URL string on the IO dispatcher.
         *
         * @param filePath the path to the hospital image file.
         */
        override suspend fun getHospitalImageUrl(filePath: String): Flow<String> =
        flow {
            val res = hospitalAPI.getHospitalImageUrl(filePath)
            emit(res)
        }.flowOn(Dispatchers.IO)

}