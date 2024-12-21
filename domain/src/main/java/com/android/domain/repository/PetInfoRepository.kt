package com.android.domain.repository

import com.android.domain.entity.PetDetailsEntity
import com.android.domain.entity.PetRequestEntity
import com.android.domain.entity.PetUpdateEntity
import com.android.domain.util.ApiResult

interface PetInfoRepository {
    suspend fun registerPet(pet: PetRequestEntity): ApiResult<PetDetailsEntity>
    suspend fun getPetDetails(petId: Long): ApiResult<PetDetailsEntity>
    suspend fun getPetImageUrl(filePath: String): ApiResult<String>
    suspend fun updatePetInfo(petId: Long, updatePetInfo: PetUpdateEntity): ApiResult<Unit>
}