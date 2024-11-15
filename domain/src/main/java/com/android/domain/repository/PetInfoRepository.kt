package com.android.domain.repository

import com.android.domain.entity.PetDetailsEntity
import com.android.domain.util.ApiResult

interface PetInfoRepository {
    suspend fun getPetDetails(petId: Long): ApiResult<PetDetailsEntity>
}