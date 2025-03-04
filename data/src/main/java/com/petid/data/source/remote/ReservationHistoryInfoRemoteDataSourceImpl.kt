package com.petid.data.source.remote

import com.petid.data.api.HospitalAPI
import com.petid.data.dto.response.HospitalOrderDetailResponse
import javax.inject.Inject

class ReservationHistoryInfoRemoteDataSourceImpl @Inject constructor(
    private val hospitalAPI: HospitalAPI
): ReservationHistoryInfoRemoteDataSource {
    override suspend fun getHospitalReservationHistoryList(
        status: String
    ): List<HospitalOrderDetailResponse> =
        hospitalAPI.getHospitalOrderList(status)

    override suspend fun cancelHospitalReservation(orderId: Int): Int =
        hospitalAPI.deleteHospitalOrder(orderId)
}