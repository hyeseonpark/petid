package com.petid.data.api

import com.petid.data.dto.request.FilePathRequest
import com.petid.data.dto.request.PetRequest
import com.petid.data.dto.response.PetDetailsResponse
import com.petid.domain.entity.PetUpdateEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PetAPI {

    /**
     * 2.1 애완동물 조회 API
     */
    @GET("/v1/pet/{petId}")
    suspend fun getPetDetails(
        @Path("petId") petId: Long
    ): PetDetailsResponse

    /**
     * 2.2 애완동물 등록 API
     */
    @POST("/v1/pet")
    suspend fun registerPet(
        @Body petRequest: PetRequest
    ): PetDetailsResponse

    /**
     * 2.3 애완동물 수정 API
     */
    @PUT("/v1/pet/{petId}")
    suspend fun updatePetInfo(
        @Path("petId") petId: Long,
        @Body updatePetInfo: PetUpdateEntity
    )

    /**
     * 2.4 애완동물 삭제 API
     */
    @DELETE("/v1/pet/{petId}")
    suspend fun deletePet(
        @Path("petId") petId: Long
    )

    /**
     * 2.5 애완동물 외형 정보 수정 API
     */
    @PUT("/v1/pet/{petId}/appearance")
    suspend fun updatePetAppearance(
        @Path("petId") petId: Long,
        //@Body appearance: PetAppearanceRequest
    )//: Response<PetAppearanceResponse>

    /**
     * 2.6 애완동물 이미지 추가 API
     */
    @POST("/v1/pet/{petId}/images")
    suspend fun addPetImage(
        @Path("petId") petId: Long,
        //@Body imagePath: ImagePathRequest
    )

    /**
     * 2.7 애완동물 이미지 업데이트
     */
    @PATCH("/v1/pet/{petId}/images/{petImageId}")
    suspend fun updatePetPhoto(
        @Path("petId") petId: Long,
        @Path("petImageId") petImageId: Long,
        @Body filePath: FilePathRequest
    )

    /**
     * 2.9 애완동물 이미지 조회 (S3) API
     */
    @GET("/v1/pet/3/images/presigned-url")
    suspend fun getPetImageUrl(
        @Query("filePath") filePath: String
    ): String

    /**
     * 2.11 애완동물 목록 조회 API
     */
    @GET("/v1/pet")
    suspend fun getPetList()
}