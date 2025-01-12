package com.petid.data.repository.remote

import com.petid.data.dto.request.toDto
import com.petid.data.source.remote.PetInfoDataSource
import com.petid.domain.entity.FilePath
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
    override suspend fun registerPet(pet: PetRequestEntity): ApiResult<PetDetailsEntity> =
        petInfoDataSource.registerPet(pet)

    override suspend fun getPetDetails(petId: Long): ApiResult<PetDetailsEntity> =
        petInfoDataSource.getPetDetails(petId)

    override suspend fun getPetImageUrl(filePath: String): ApiResult<String> =
        petInfoDataSource.getPetImageUrl(filePath)

    override suspend fun updatePetInfo(
        petId: Long,
        updatePetInfo: PetUpdateEntity
    ): ApiResult<Unit> =
        petInfoDataSource.updatePetInfo(petId, updatePetInfo)

    override suspend fun updatePetPhoto(
        petId: Long,
        petImageId: Long,
        filePath: FilePath
    ): ApiResult<Unit> =
        petInfoDataSource.updatePetPhoto(petId, petImageId, filePath.toDto())
}