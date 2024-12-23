package com.petid.data.source.remote

import com.petid.domain.entity.HospitalOrderDetailEntity
import com.petid.domain.util.ApiResult

interface ReservationHistoryInfoRemoteDataSource {
    suspend fun getHospitalReservationHistoryList(status: String): ApiResult<List<HospitalOrderDetailEntity>>
    suspend fun cancelHospitalReservation(orderId: Int): ApiResult<Int>
}