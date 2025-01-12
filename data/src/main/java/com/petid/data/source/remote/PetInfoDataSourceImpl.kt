package com.petid.data.source.remote

import com.petid.data.api.PetAPI
import com.petid.data.dto.request.FilePathRequest
import com.petid.data.dto.response.toDomain
import com.petid.data.util.mapApiResult
import com.petid.domain.entity.PetDetailsEntity
import com.petid.domain.entity.PetRequestEntity
import com.petid.domain.entity.PetUpdateEntity
import com.petid.domain.util.ApiResult
import javax.inject.Inject

class PetInfoDataSourceImpl @Inject constructor(
    private val petAPI: PetAPI
): PetInfoDataSource {

    override suspend fun registerPet(pet: PetRequestEntity): ApiResult<PetDetailsEntity> =
        runCatching {
            petAPI.registerPet(pet).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getPetDetails(petId: Long): ApiResult<PetDetailsEntity> =
        runCatching {
            petAPI.getPetDetails(petId).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getPetImageUrl(filePath: String): ApiResult<String> =
        runCatching {
            petAPI.getPetImageUrl(filePath)
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun updatePetInfo(
        petId: Long,
        updatePetInfo: PetUpdateEntity
    ): ApiResult<Unit> =
        runCatching {
            petAPI.updatePetInfo(petId, updatePetInfo)
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun updatePetPhoto(
        petId: Long,
        petImageId: Long,
        filePathRequest: FilePathRequest
    ): ApiResult<Unit> =
        runCatching {
            petAPI.updatePetPhoto(petId, petImageId, filePathRequest)
        }.mapApiResult { ApiResult.Success(it) }
}