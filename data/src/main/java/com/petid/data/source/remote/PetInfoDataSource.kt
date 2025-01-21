package com.petid.data.source.remote

import com.petid.data.dto.request.FilePathRequest
import com.petid.data.dto.request.PetRequest
import com.petid.domain.entity.PetDetailsEntity
import com.petid.domain.entity.PetUpdateEntity
import com.petid.domain.util.ApiResult

interface PetInfoDataSource {
    suspend fun registerPet(petRequest: PetRequest): ApiResult<PetDetailsEntity>
    suspend fun getPetDetails(petId: Long): ApiResult<PetDetailsEntity>
    suspend fun getPetImageUrl(filePath: String): ApiResult<String>
    suspend fun updatePetInfo(petId: Long, updatePetInfo: PetUpdateEntity): ApiResult<Unit>
    suspend fun updatePetPhoto(petId: Long, petImageId: Long, filePathRequest: FilePathRequest): ApiResult<Unit>
}