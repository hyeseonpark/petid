package com.petid.data.source.remote

import com.petid.data.api.HosptialAPI
import com.petid.data.dto.request.HospitalOrderRequest
import com.petid.data.dto.response.ErrorResponse
import com.petid.data.dto.response.toDomain
import com.petid.domain.entity.HospitalOrderEntity
import com.petid.domain.util.ApiResult
import com.google.gson.Gson
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
    ): ApiResult<List<String>> {
        return try {
            val response = hosptialAPI.getHospitalOrderTimeList(hospitalId, day, date)
            ApiResult.Success(response)

        } catch (e: HttpException) {
            val gson = Gson()
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)

            ApiResult.HttpError(errorResponse.toDomain())

        } catch (e: Exception) {
            ApiResult.Error(e.message)
        }
    }

    override suspend fun createHospitalOrder(
        hospitalId: Int,
        date: String
    ): ApiResult<HospitalOrderEntity> {
        return try {
            val response = hosptialAPI.createHospitalOrder(
                HospitalOrderRequest(hospitalId, date)
            )
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