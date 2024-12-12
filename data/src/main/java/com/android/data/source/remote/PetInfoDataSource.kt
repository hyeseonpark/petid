package com.android.data.source.remote

import com.android.domain.entity.PetDetailsEntity
import com.android.domain.entity.PetRequestEntity
import com.android.domain.util.ApiResult

interface PetInfoDataSource {
    suspend fun registerPet(pet: PetRequestEntity): ApiResult<PetDetailsEntity>
    suspend fun getPetDetails(petId: Long): ApiResult<PetDetailsEntity>
    suspend fun getPetImageUrl(filePath: String): ApiResult<String>
}