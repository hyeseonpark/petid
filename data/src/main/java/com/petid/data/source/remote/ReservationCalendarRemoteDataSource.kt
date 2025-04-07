package com.petid.data.source.remote

import com.petid.data.dto.request.HospitalOrderRequest
import com.petid.data.dto.response.HospitalOrderResponse
import com.petid.data.dto.response.HospitalResponse

interface ReservationCalendarRemoteDataSource {
    suspend fun getHospitalOrderTimeList(hospitalId: Long, day: String, date: String): List<String>
    suspend fun createHospitalOrder(hospitalOrderRequest: HospitalOrderRequest): HospitalOrderResponse
    suspend fun getHospitalDetailById(id: Long): HospitalResponse
}