package com.android.data.source.remote

import com.android.domain.entity.HospitalOrderEntity
import com.android.domain.util.ApiResult
import java.util.Date

interface ReservationCalendarDataSource {
    suspend fun getHospitalOrderTimeList(hospitalId: Int, day: String, date: String): ApiResult<List<String>>
    suspend fun createHospitalOrder(hospitalId: Int, date: Date): ApiResult<HospitalOrderEntity>
}