package com.petid.domain.repository

import com.petid.domain.entity.HospitalOrderDetailEntity
import com.petid.domain.util.ApiResult

interface ReservationHistoryInfoRepository {
    suspend fun getHospitalReservationHistoryList(status: String): ApiResult<List<HospitalOrderDetailEntity>>
    suspend fun cancelHospitalReservation(orderId: Int): ApiResult<Int>
}