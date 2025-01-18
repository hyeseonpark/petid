package com.petid.domain.repository

import com.petid.domain.entity.FilePath
import com.petid.domain.entity.PetDetailsEntity
import com.petid.domain.entity.Pet
import com.petid.domain.entity.PetUpdateEntity
import com.petid.domain.util.ApiResult

interface PetInfoRepository {
    suspend fun registerPet(pet: Pet): ApiResult<PetDetailsEntity>
    suspend fun getPetDetails(petId: Long): ApiResult<PetDetailsEntity>
    suspend fun getPetImageUrl(filePath: String): ApiResult<String>
    suspend fun updatePetInfo(petId: Long, updatePetInfo: PetUpdateEntity): ApiResult<Unit>
    suspend fun updatePetPhoto(petId: Long, petImageId: Long, filePath: FilePath): ApiResult<Unit>
}