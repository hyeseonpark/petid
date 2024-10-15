package com.android.domain.repository

import com.android.domain.entity.HospitalOrderDetailEntity
import com.android.domain.util.ApiResult

interface ReservationHistoryInfoRepository {
    suspend fun getHospitalReservationHistoryList(status: String): ApiResult<List<HospitalOrderDetailEntity>>
}