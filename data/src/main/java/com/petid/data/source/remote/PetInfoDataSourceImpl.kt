package com.petid.data.source.remote

import com.petid.data.api.PetAPI
import com.petid.data.dto.request.FilePathRequest
import com.petid.data.dto.request.PetRequest
import com.petid.data.dto.request.PetUpdateRequest
import com.petid.data.dto.response.PetDetailsResponse
import javax.inject.Inject

class PetInfoDataSourceImpl @Inject constructor(
    private val petAPI: PetAPI
): PetInfoDataSource {

    override suspend fun registerPet(petRequest: PetRequest): PetDetailsResponse =
        petAPI.registerPet(petRequest)

    override suspend fun getPetDetails(petId: Long): PetDetailsResponse =
        petAPI.getPetDetails(petId)

    override suspend fun getPetImageUrl(filePath: String): String =
        petAPI.getPetImageUrl(filePath)

    override suspend fun updatePetInfo(
        petId: Long,
        updatePetInfo: PetUpdateRequest
    ): Unit =
        petAPI.updatePetInfo(petId, updatePetInfo)

    override suspend fun updatePetPhoto(
        petId: Long,
        petImageId: Long,
        filePathRequest: FilePathRequest
    ): Unit =
        petAPI.updatePetPhoto(petId, petImageId, filePathRequest)
}