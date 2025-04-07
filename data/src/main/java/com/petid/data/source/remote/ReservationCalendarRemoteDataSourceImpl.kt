package com.petid.data.source.remote

import com.petid.data.api.HospitalAPI
import com.petid.data.dto.request.HospitalOrderRequest
import com.petid.data.dto.response.HospitalOrderResponse
import com.petid.data.dto.response.HospitalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationCalendarRemoteDataSourceImpl @Inject constructor(
    private val hospitalAPI: HospitalAPI
): ReservationCalendarRemoteDataSource {
    override suspend fun getHospitalOrderTimeList(
        hospitalId: Long,
        day: String,
        date: String
    ): List<String> =
        hospitalAPI.getHospitalOrderTimeList(hospitalId, day, date)

    override suspend fun createHospitalOrder(
        hospitalOrderRequest: HospitalOrderRequest,
    ): HospitalOrderResponse =
        hospitalAPI.createHospitalOrder(hospitalOrderRequest)

    override suspend fun getHospitalDetailById(id: Long): HospitalResponse =
        hospitalAPI.getHospitalDetailById(id)
}