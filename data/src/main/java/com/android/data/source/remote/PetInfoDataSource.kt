package com.android.data.source.remote

import com.android.domain.entity.PetDetailsEntity
import com.android.domain.entity.PetRequestEntity
import com.android.domain.entity.PetUpdateEntity
import com.android.domain.util.ApiResult

interface PetInfoDataSource {
    suspend fun registerPet(pet: PetRequestEntity): ApiResult<PetDetailsEntity>
    suspend fun getPetDetails(petId: Long): ApiResult<PetDetailsEntity>
    suspend fun getPetImageUrl(filePath: String): ApiResult<String>
    suspend fun updatePetInfo(petId: Long, updatePetInfo: PetUpdateEntity): ApiResult<Unit>
    suspend fun updatePetPhoto(petId: Long, petImageId: Long, filePath: String): ApiResult<Unit>
}