package com.petid.data.repository.remote

import com.petid.data.dto.request.toDto
import com.petid.data.dto.response.toDomain
import com.petid.data.source.remote.ReservationCalendarRemoteDataSource
import com.petid.data.util.mapApiResult
import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.HospitalOrderEntity
import com.petid.domain.repository.ReservationCalendarRepository
import com.petid.domain.util.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationCalendarRepositoryImpl @Inject constructor(
    private val remoteDataSource: ReservationCalendarRemoteDataSource
): ReservationCalendarRepository{
    override suspend fun getHospitalOrderTimeList(
        hospitalId: Long,
        day: String,
        date: String
    ): ApiResult<List<String>> =
        runCatching {
            remoteDataSource.getHospitalOrderTimeList(hospitalId, day, date)
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun createHospitalOrder(
        hospitalOrderEntity: HospitalOrderEntity
    ): ApiResult<HospitalOrderEntity> =
        runCatching {
            remoteDataSource.createHospitalOrder(hospitalOrderEntity.toDto()).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getHospitalDetailById(id: Long): Flow<HospitalEntity> =
        flow {
            val res = remoteDataSource.getHospitalDetailById(id).toDomain()
            emit(res)
        }.flowOn(Dispatchers.IO)

}