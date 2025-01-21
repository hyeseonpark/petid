package com.petid.data.source.remote

import com.petid.data.api.HosptialAPI
import com.petid.data.dto.request.HospitalOrderRequest
import com.petid.data.dto.response.ErrorResponse
import com.petid.data.dto.response.toDomain
import com.petid.domain.entity.HospitalOrderEntity
import com.petid.domain.util.ApiResult
import com.google.gson.Gson
import com.petid.data.util.mapApiResult
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationCalendarRemoteDataSourceImpl @Inject constructor(
    private val hosptialAPI: HosptialAPI
): ReservationCalendarRemoteDataSource {
    override suspend fun getHospitalOrderTimeList(
        hospitalId: Int,
        day: String,
        date: String
    ): ApiResult<List<String>> =
        runCatching {
            hosptialAPI.getHospitalOrderTimeList(hospitalId, day, date)
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun createHospitalOrder(
        hospitalId: Int,
        date: String
    ): ApiResult<HospitalOrderEntity> =
        runCatching {
            hosptialAPI.createHospitalOrder(
                HospitalOrderRequest(hospitalId, date)
            ).toDomain()
        }.mapApiResult { ApiResult.Success(it) }
}