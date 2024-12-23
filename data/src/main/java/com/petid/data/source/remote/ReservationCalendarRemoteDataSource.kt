package com.petid.data.source.remote

import com.petid.data.dto.request.HospitalOrderRequest
import com.petid.domain.entity.HospitalOrderEntity
import com.petid.domain.util.ApiResult
import java.util.Date

interface ReservationCalendarRemoteDataSource {
    suspend fun getHospitalOrderTimeList(hospitalId: Int, day: String, date: String): ApiResult<List<String>>
    suspend fun createHospitalOrder(hospitalId: Int, date: String): ApiResult<HospitalOrderEntity>
//    suspend fun createHospitalOrder(hospitalOrderRequest: HospitalOrderRequest): ApiResult<HospitalOrderEntity>
}