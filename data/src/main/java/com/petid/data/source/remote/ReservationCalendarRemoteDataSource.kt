package com.petid.data.source.remote

import com.petid.data.dto.request.HospitalOrderRequest
import com.petid.data.dto.response.HospitalOrderResponse

interface ReservationCalendarRemoteDataSource {
    suspend fun getHospitalOrderTimeList(hospitalId: Int, day: String, date: String): List<String>
    suspend fun createHospitalOrder(hospitalOrderRequest: HospitalOrderRequest): HospitalOrderResponse
}