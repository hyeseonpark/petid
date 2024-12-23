package com.petid.domain.repository

import com.petid.domain.entity.HospitalOrderEntity
import com.petid.domain.util.ApiResult
import java.util.Date

interface ReservationCalendarRepository {
    suspend fun getHospitalOrderTimeList(hospitalId: Int, day: String, date: String): ApiResult<List<String>>
    suspend fun createHospitalOrder(hospitalId: Int, date: String): ApiResult<HospitalOrderEntity>
//    suspend fun createHospitalOrder(hospitalOrderRequest: HospitalOrderRequestd): ApiResult<HospitalOrderEntity>
}