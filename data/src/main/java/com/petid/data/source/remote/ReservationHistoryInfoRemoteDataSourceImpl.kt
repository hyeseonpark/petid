package com.petid.data.source.remote

import com.petid.data.api.HosptialAPI
import com.petid.data.dto.response.ErrorResponse
import com.petid.data.dto.response.toDomain
import com.petid.domain.entity.HospitalOrderDetailEntity
import com.petid.domain.util.ApiResult
import com.google.gson.Gson
import com.petid.data.util.mapApiResult
import retrofit2.HttpException
import javax.inject.Inject

class ReservationHistoryInfoRemoteDataSourceImpl @Inject constructor(
    private val hosptialAPI: HosptialAPI
): ReservationHistoryInfoRemoteDataSource {
    override suspend fun getHospitalReservationHistoryList(
        status: String
    ): ApiResult<List<HospitalOrderDetailEntity>> =
        runCatching {
            hosptialAPI.getHospitalOrderList(status).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun cancelHospitalReservation(orderId: Int): ApiResult<Int> =
        runCatching {
            hosptialAPI.deleteHospitalOrder(orderId)
        }.mapApiResult { ApiResult.Success(it) }
}