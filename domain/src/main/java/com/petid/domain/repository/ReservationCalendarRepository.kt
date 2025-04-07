package com.petid.domain.repository

import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.HospitalOrderEntity
import com.petid.domain.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface ReservationCalendarRepository {
    suspend fun getHospitalOrderTimeList(hospitalId: Long, day: String, date: String): ApiResult<List<String>>
    suspend fun createHospitalOrder(hospitalOrderEntity: HospitalOrderEntity): ApiResult<HospitalOrderEntity>
    suspend fun getHospitalDetailById(id: Long): Flow<HospitalEntity>
}