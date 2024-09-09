package com.android.data.source.remote

import android.util.Log
import com.android.data.api.HosptialAPI
import com.android.data.api.LocationAPI
import com.android.data.model.ErrorResponse
import com.android.data.model.toDomain
import com.android.domain.entity.HospitalEntity
import com.android.domain.entity.LocationEntity
import com.android.domain.util.ApiResult
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HospitalMainRemoteDataSourceImpl @Inject constructor(
    private val locationAPI: LocationAPI,
    private val hosptialAPI: HosptialAPI
) : HospitalMainRemoteDataSource {
    override suspend fun getSido(): ApiResult<List<LocationEntity>> {
        return try {
            val response = locationAPI.getSidoList()
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

    override suspend fun getSigunguList(id: Int): ApiResult<List<LocationEntity>> {
        return try {
            val response = locationAPI.getSigunguList(id)
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

    override suspend fun getEupmundongList(id: Int): ApiResult<List<LocationEntity>> {
        return try {
            val response = locationAPI.getEupmyeondongList(id)
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

    override suspend fun getHospitalList(
        sidoId: Int,
        sigunguId: Int,
        eupmundongId: Int
    ): ApiResult<List<HospitalEntity>> {
        return try {
            val response = hosptialAPI.getHospitalList(sidoId, sigunguId, eupmundongId)
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