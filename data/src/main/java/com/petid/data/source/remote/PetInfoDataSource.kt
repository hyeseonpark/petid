package com.petid.data.source.remote

import com.petid.data.dto.request.FilePathRequest
import com.petid.data.dto.request.PetRequest
import com.petid.data.dto.response.PetDetailsResponse
import com.petid.domain.entity.PetUpdateEntity

interface PetInfoDataSource {
    suspend fun registerPet(petRequest: PetRequest): PetDetailsResponse
    suspend fun getPetDetails(petId: Long): PetDetailsResponse
    suspend fun getPetImageUrl(filePath: String): String
    suspend fun updatePetInfo(petId: Long, updatePetInfo: PetUpdateEntity): Unit
    suspend fun updatePetPhoto(petId: Long, petImageId: Long, filePathRequest: FilePathRequest): Unit
}