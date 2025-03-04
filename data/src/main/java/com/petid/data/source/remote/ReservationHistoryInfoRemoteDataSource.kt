package com.petid.data.source.remote

import com.petid.data.dto.response.HospitalOrderDetailResponse

interface ReservationHistoryInfoRemoteDataSource {
    suspend fun getHospitalReservationHistoryList(status: String): List<HospitalOrderDetailResponse>
    suspend fun cancelHospitalReservation(orderId: Int): Int
}