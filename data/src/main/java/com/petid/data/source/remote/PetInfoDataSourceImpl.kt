package com.petid.data.source.remote

import com.petid.data.api.PetAPI
import com.petid.data.dto.response.ErrorResponse
import com.petid.data.dto.response.toDomain
import com.petid.domain.entity.PetDetailsEntity
import com.petid.domain.entity.PetRequestEntity
import com.petid.domain.entity.PetUpdateEntity
import com.petid.domain.util.ApiResult
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class PetInfoDataSourceImpl @Inject constructor(
    private val petAPI: PetAPI
): PetInfoDataSource {

    override suspend fun registerPet(pet: PetRequestEntity): ApiResult<PetDetailsEntity> {
        return try {
            val response = petAPI.registerPet(pet)
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

    override suspend fun getPetDetails(petId: Long): ApiResult<PetDetailsEntity> {
        return try {
            val response = petAPI.getPetDetails(petId)
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

    override suspend fun getPetImageUrl(filePath: String): ApiResult<String> {
        return try {
            val response = petAPI.getPetImageUrl(filePath)
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

    override suspend fun updatePetInfo(
        petId: Long,
        updatePetInfo: PetUpdateEntity
    ): ApiResult<Unit> {
        return try {
            val response = petAPI.updatePetInfo(petId, updatePetInfo)
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

    override suspend fun updatePetPhoto(
        petId: Long,
        petImageId: Long,
        filePath: String
    ): ApiResult<Unit> {
        return try {
            val response = petAPI.updatePetPhoto(petId, petImageId, filePath)
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