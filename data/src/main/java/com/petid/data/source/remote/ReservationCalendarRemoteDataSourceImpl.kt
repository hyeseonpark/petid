package com.petid.data.source.remote

import com.petid.data.api.HospitalAPI
import com.petid.data.dto.request.HospitalOrderRequest
import com.petid.data.dto.response.HospitalOrderResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationCalendarRemoteDataSourceImpl @Inject constructor(
    private val hospitalAPI: HospitalAPI
): ReservationCalendarRemoteDataSource {
    override suspend fun getHospitalOrderTimeList(
        hospitalId: Int,
        day: String,
        date: String
    ): List<String> =
        hospitalAPI.getHospitalOrderTimeList(hospitalId, day, date)

    override suspend fun createHospitalOrder(
        hospitalOrderRequest: HospitalOrderRequest,
    ): HospitalOrderResponse =
        hospitalAPI.createHospitalOrder(hospitalOrderRequest)
}