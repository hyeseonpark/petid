package com.android.data.source.remote

import com.android.data.api.HosptialAPI
import com.android.data.model.ErrorResponse
import com.android.data.model.toDomain
import com.android.domain.entity.HospitalOrderEntity
import com.android.domain.util.ApiResult
import com.google.gson.Gson
import retrofit2.HttpException
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationCalendarDataSourceImpl @Inject constructor(
    private val hosptialAPI: HosptialAPI
): ReservationCalendarDataSource {
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
        date: Date
    ): ApiResult<HospitalOrderEntity> {
        return try {
            val response = hosptialAPI.createHospitalOrder(hospitalId, date)
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