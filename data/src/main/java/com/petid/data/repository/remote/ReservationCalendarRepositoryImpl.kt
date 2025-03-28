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
        hospitalId: Int,
        day: String,
        date: String
    ): ApiResult<List<String>> =
        runCatching {
            remoteDataSource.getHospitalOrderTimeList(hospitalId, day, date)
        }.mapApiResult { ApiResult.Success(it) }

    /**
         * Creates a new hospital order.
         *
         * Converts the provided hospital order entity into a DTO, sends it to the remote data source to create the hospital order,
         * and converts the response back into a domain entity. The result is wrapped in an ApiResult.Success.
         *
         * @param hospitalOrderEntity the hospital order details to be processed.
         * @return an ApiResult containing the created hospital order as a domain entity.
         */
        override suspend fun createHospitalOrder(
        hospitalOrderEntity: HospitalOrderEntity
    ): ApiResult<HospitalOrderEntity> =
        runCatching {
            remoteDataSource.createHospitalOrder(hospitalOrderEntity.toDto()).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    /**
         * Retrieves hospital details as a flow.
         *
         * This suspend function calls the remote data source to fetch hospital details for the given hospital identifier, converts the response to its domain entity, and emits it in a flow executed on the IO dispatcher.
         *
         * @param id the unique identifier of the hospital.
         * @return a Flow emitting the corresponding hospital entity.
         */
        override suspend fun getHospitalDetailById(id: Int): Flow<HospitalEntity> =
        flow {
            val res = remoteDataSource.getHospitalDetailById(id).toDomain()
            emit(res)
        }.flowOn(Dispatchers.IO)

}