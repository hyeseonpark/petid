package com.android.data.source.remote

import com.android.domain.entity.HospitalOrderDetailEntity
import com.android.domain.util.ApiResult

interface ReservationHistoryInfoRemoteDataSource {
    suspend fun getHospitalReservationHistoryList(status: String): ApiResult<List<HospitalOrderDetailEntity>>
    suspend fun cancelHospitalReservation(orderId: Int): ApiResult<Int>
}