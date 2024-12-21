package com.android.data.repository

import com.android.data.source.remote.PetInfoDataSource
import com.android.domain.entity.PetDetailsEntity
import com.android.domain.entity.PetRequestEntity
import com.android.domain.entity.PetUpdateEntity
import com.android.domain.repository.PetInfoRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetInfoRepositoryImpl @Inject constructor(
    private val petInfoDataSource: PetInfoDataSource,
): PetInfoRepository {

    override suspend fun registerPet(pet: PetRequestEntity): ApiResult<PetDetailsEntity> {
        return when (val result = petInfoDataSource.registerPet(pet)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun getPetDetails(petId: Long): ApiResult<PetDetailsEntity> {
        return when (val result = petInfoDataSource.getPetDetails(petId)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun getPetImageUrl(filePath: String): ApiResult<String> {
        return when (val result = petInfoDataSource.getPetImageUrl(filePath)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun updatePetInfo(
        petId: Long,
        updatePetInfo: PetUpdateEntity
    ): ApiResult<Unit> {
        return when (val result = petInfoDataSource.updatePetInfo(petId, updatePetInfo)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }
}