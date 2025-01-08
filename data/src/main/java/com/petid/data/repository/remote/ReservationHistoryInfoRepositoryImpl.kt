package com.petid.data.repository.remote

import com.petid.data.source.remote.ReservationHistoryInfoRemoteDataSource
import com.petid.domain.entity.HospitalOrderDetailEntity
import com.petid.domain.repository.ReservationHistoryInfoRepository
import com.petid.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationHistoryInfoRepositoryImpl @Inject constructor(
    private val remoteDataSource: ReservationHistoryInfoRemoteDataSource,
): ReservationHistoryInfoRepository {
    override suspend fun getHospitalReservationHistoryList(
        status: String,
    ): ApiResult<List<HospitalOrderDetailEntity>> =
        remoteDataSource.getHospitalReservationHistoryList(status)

    override suspend fun cancelHospitalReservation(orderId: Int): ApiResult<Int> =
        remoteDataSource.cancelHospitalReservation(orderId)
}