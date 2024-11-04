package com.android.data.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
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
    )//: Response<PetDetailsResponse>

    /**
     * 2.2 애완동물 등록 API
     */
    @POST("/v1/pet")
    suspend fun registerPet(
        //@Body pet: RegisterPetRequest
    )//: Response<PetDetailsResponse>

    /**
     * 2.3 애완동물 수정 API
     */
    @PUT("/v1/pet")
    suspend fun updatePet(
       // @Body pet: UpdatePetRequest
    )//: Response<PetDetailsResponse>

    /**
     * 2.4 애완동물 삭제 API
     */
    @DELETE("/v1/pet/{petId}")
    suspend fun deletePet(
        @Path("petId") petId: Long
    )//: Response<Void>

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
    )//: Response<PetImageResponse>

    /**
     * 2.7 애완동물 이미지 등록 (S3) API
     */
    @POST("/v1/pet/{petId}/images/presigned-url")
    suspend fun getPresignedUrlForImageUpload(
        @Path("petId") petId: Long,
        @Body filePath: String
    )//: Response<String>

    /**
     * 2.8 애완동물 이미지 조회 (S3) API
     */
    @GET("/v1/pet/{petId}/images/presigned-url")
    suspend fun getPetImageUrl(
        @Path("petId") petId: Long,
        @Query("filePath") filePath: String
    )//: Response<String>

    /**
     * 2.9 애완동물 목록 조회 API
     */
    @GET("/v1/pet")
    suspend fun getPetList()//: Response<List<PetSummaryResponse>>
}