package com.petid.data.repository

import com.petid.data.dto.request.HospitalOrderRequest
import com.petid.data.source.remote.ReservationCalendarRemoteDataSource
import com.petid.domain.entity.HospitalOrderEntity
import com.petid.domain.repository.ReservationCalendarRepository
import com.petid.domain.util.ApiResult
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
    ): ApiResult<List<String>> =
        remoteDataSource.getHospitalOrderTimeList(hospitalId, day, date)

    override suspend fun createHospitalOrder(
        hospitalId: Int,
        date: String
    ): ApiResult<HospitalOrderEntity> =
        remoteDataSource.createHospitalOrder(hospitalId, date)
}