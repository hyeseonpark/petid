package com.petid.data.source.remote

import com.petid.data.api.BannerAPI
import com.petid.data.dto.request.PresignedGetUrlRequest
import com.petid.data.dto.response.ErrorResponse
import com.petid.data.dto.response.toDomain
import com.petid.domain.entity.BannerEntity
import com.petid.domain.util.ApiResult
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeMainRemoteDataSourceImpl @Inject constructor(
    private val bannerAPI: BannerAPI,
) : HomeMainRemoteDataSource{
    override suspend fun getBannerList(type: String): ApiResult<List<BannerEntity>> {
        return try {
            val response = bannerAPI.getBannerList(type)
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

    override suspend fun getBannerImage(imagePath: String): ApiResult<String> {
        return try {
            val response = bannerAPI.getBannerImage(
                imagePath
//                PresignedGetUrlRequest(imagePath)
            )
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
}