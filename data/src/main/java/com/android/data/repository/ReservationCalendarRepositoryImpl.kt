package com.android.data.repository

import com.android.data.dto.request.HospitalOrderRequest
import com.android.data.source.remote.ReservationCalendarRemoteDataSource
import com.android.domain.entity.HospitalOrderEntity
import com.android.domain.repository.ReservationCalendarRepository
import com.android.domain.util.ApiResult
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationCalendarRepositoryImpl @Inject constructor(
    private val remoteDataSource: ReservationCalendarRemoteDataSource
): ReservationCalendarRepository{
    override suspend fun getHospitalOrderTimeList(
        hospitalId: Int,
        day: String,
        date: String
    ): ApiResult<List<String>> {
        return when (val result = remoteDataSource.getHospitalOrderTimeList(hospitalId, day, date)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun createHospitalOrder(
        hospitalId: Int,
        date: String
    ): ApiResult<HospitalOrderEntity> {
        return when (val result = remoteDataSource.createHospitalOrder(hospitalId, date)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }
}