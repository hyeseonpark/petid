package com.android.data.source.remote

import com.android.data.api.HosptialAPI
import com.android.data.dto.response.ErrorResponse
import com.android.data.dto.response.toDomain
import com.android.domain.entity.HospitalOrderDetailEntity
import com.android.domain.util.ApiResult
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class ReservationHistoryInfoRemoteDataSourceImpl @Inject constructor(
    private val hosptialAPI: HosptialAPI
): ReservationHistoryInfoRemoteDataSource {
    override suspend fun getHospitalReservationHistoryList(
        status: String
    ): ApiResult<List<HospitalOrderDetailEntity>> {
        return try {
            val response = hosptialAPI.getHospitalOrderList(status)
            ApiResult.Success(response.toDomain())

        } catch (e: HttpException) {
            val gson = Gson()
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)

            ApiResult.HttpError(errorResponse.toDomain())

        } catch (e: Exception) {
            ApiResult.Error(e.message)
        }
    }
}