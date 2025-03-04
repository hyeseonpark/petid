package com.petid.data.repository.remote

import com.petid.data.dto.request.toDto
import com.petid.data.dto.response.toDomain
import com.petid.data.source.remote.PetInfoDataSource
import com.petid.data.util.mapApiResult
import com.petid.domain.entity.FilePath
import com.petid.domain.entity.PetDetailsEntity
import com.petid.domain.entity.Pet
import com.petid.domain.entity.PetUpdateEntity
import com.petid.domain.repository.PetInfoRepository
import com.petid.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetInfoRepositoryImpl @Inject constructor(
    private val petInfoDataSource: PetInfoDataSource,
): PetInfoRepository {
    override suspend fun registerPet(pet: Pet): ApiResult<PetDetailsEntity> =
        runCatching {
            petInfoDataSource.registerPet(pet.toDto()).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getPetDetails(petId: Long): ApiResult<PetDetailsEntity> =
        runCatching {
            petInfoDataSource.getPetDetails(petId).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getPetImageUrl(filePath: String): ApiResult<String> =
        runCatching {
            petInfoDataSource.getPetImageUrl(filePath)
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun updatePetInfo(
        petId: Long,
        updatePetInfo: PetUpdateEntity
    ): ApiResult<Unit> =
        runCatching {
            petInfoDataSource.updatePetInfo(petId, updatePetInfo)
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun updatePetPhoto(
        petId: Long,
        petImageId: Long,
        filePath: FilePath
    ): ApiResult<Unit> =
        runCatching {
            petInfoDataSource.updatePetPhoto(petId, petImageId, filePath.toDto())
        }.mapApiResult { ApiResult.Success(it) }
}