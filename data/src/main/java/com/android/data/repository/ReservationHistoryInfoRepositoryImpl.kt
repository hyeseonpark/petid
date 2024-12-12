package com.android.data.repository

import com.android.data.api.HosptialAPI
import com.android.data.dto.request.DeleteHospitalOrderRequest
import com.android.data.source.remote.ReservationHistoryInfoRemoteDataSource
import com.android.domain.entity.HospitalOrderDetailEntity
import com.android.domain.repository.ReservationHistoryInfoRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationHistoryInfoRepositoryImpl @Inject constructor(
    private val remoteDataSource: ReservationHistoryInfoRemoteDataSource,
): ReservationHistoryInfoRepository {
    override suspend fun getHospitalReservationHistoryList(status: String): ApiResult<List<HospitalOrderDetailEntity>> {
        return when (val result = remoteDataSource.getHospitalReservationHistoryList(status)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun cancelHospitalReservation(orderId: Int): ApiResult<Int> {
        return when (val result = remoteDataSource.cancelHospitalReservation(orderId)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }
}