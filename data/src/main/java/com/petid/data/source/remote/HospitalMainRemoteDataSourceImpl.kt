package com.petid.data.source.remote

import com.petid.data.api.HosptialAPI
import com.petid.data.api.LocationAPI
import com.petid.data.dto.response.ErrorResponse
import com.petid.data.dto.response.toDomain
import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.LocationEntity
import com.petid.domain.util.ApiResult
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

    override suspend fun getHospitalListLoc(
        sidoId: Int,
        sigunguId: Int,
        eupmundongId: Int,
        lat: Double,
        lon: Double
    ): ApiResult<List<HospitalEntity>> {
        return try {
            val response = hosptialAPI.getHospitalListByLocation(
                sidoId, sigunguId, eupmundongId, lat, lon)
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