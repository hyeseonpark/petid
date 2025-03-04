package com.petid.data.source.remote

import com.petid.data.dto.request.FilePathRequest
import com.petid.data.dto.request.PetRequest
import com.petid.data.dto.request.PetUpdateRequest
import com.petid.data.dto.response.PetDetailsResponse

interface PetInfoDataSource {
    suspend fun registerPet(petRequest: PetRequest): PetDetailsResponse
    suspend fun getPetDetails(petId: Long): PetDetailsResponse
    suspend fun getPetImageUrl(filePath: String): String
    suspend fun updatePetInfo(petId: Long, updatePetInfo: PetUpdateRequest): Unit
    suspend fun updatePetPhoto(petId: Long, petImageId: Long, filePathRequest: FilePathRequest): Unit
}