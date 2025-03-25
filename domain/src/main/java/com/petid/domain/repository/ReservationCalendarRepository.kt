package com.petid.domain.repository

import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.HospitalOrderEntity
import com.petid.domain.util.ApiResult

interface ReservationCalendarRepository {
    suspend fun getHospitalOrderTimeList(hospitalId: Int, day: String, date: String): ApiResult<List<String>>
    suspend fun createHospitalOrder(hospitalOrderEntity: HospitalOrderEntity): ApiResult<HospitalOrderEntity>
    suspend fun getHospitalDetailById(id: Int): ApiResult<HospitalEntity>
}