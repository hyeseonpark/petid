package com.petid.data.repository

import com.petid.data.source.remote.PetInfoDataSource
import com.petid.domain.entity.PetDetailsEntity
import com.petid.domain.entity.PetRequestEntity
import com.petid.domain.entity.PetUpdateEntity
import com.petid.domain.repository.PetInfoRepository
import com.petid.domain.util.ApiResult
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

    override suspend fun updatePetPhoto(
        petId: Long,
        petImageId: Long,
        filePath: String
    ): ApiResult<Unit> {
        return when (val result = petInfoDataSource.updatePetPhoto(petId, petImageId, filePath)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }
}