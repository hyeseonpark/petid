package com.petid.data.api

import com.petid.data.dto.request.DeleteHospitalOrderRequest
import com.petid.data.dto.request.HospitalOrderRequest
import com.petid.data.dto.response.HospitalOrderDetailResponse
import com.petid.data.dto.response.HospitalOrderResponse
import com.petid.data.dto.response.HospitalResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HosptialAPI {

    /**
     * 병원 리스트 조회
     */
    @GET("/v1/hospital")
    suspend fun getHospitalList(
        @Query("sido") sido: Int,
        @Query("sigungu") sigungu: Int,
        @Query("eupmundong") eupmundong: Int,
    ) : List<HospitalResponse>

    /**
     * 병원 리스트 조회(거리 순 정렬)
     */
    @GET("/v1/hospital/location")
    suspend fun getHospitalListByLocation(
        @Query("sido") sido: Int,
        @Query("sigungu") sigungu: Int,
        @Query("eupmundong") eupmundong: Int,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ) : List<HospitalResponse>

    /**
     * 동물병원 예약 가능 시간 리스트 조회
     */
    @GET("/v1/hospital/order/time")
    suspend fun getHospitalOrderTimeList(
        @Query("hospitalId") hospitalId: Int,
        @Query("day") day: String,
        @Query("date") date: String // yyyy-mm-dd
    ) : List<String>

    /**
     * 4.5 병원 이미지 조회 API
     */
    @GET("/v1/hospital/images/presigned-url")
    suspend fun getHospitalImageUrl(
        @Query("filePath") filePath: String
    ): String

    /**
     * 동물병원 예약 내역 생성
     */
    @POST("/v1/hospital/order")
    suspend fun createHospitalOrder(
        @Body request: HospitalOrderRequest
    ) : HospitalOrderResponse

    /**
     * 동물병원 예약 내역 수정
     */
    @PATCH("/v1/hospital/order")
    suspend fun updateHospitalOrder(
        @Field("orderId") orderId: Int,
        @Field("date") date: String // ISO-8601
    ) : HospitalOrderResponse

    /**
     * 동물병원 예약 내역 취소
     */
    @DELETE("/v1/hospital/order/{orderId}")
    suspend fun deleteHospitalOrder(
        @Path("orderId") orderId: Int,
    ) : Int

    /**
     * 동물병원 예약 목록 조회
     */
    @GET("/v1/hospital/order")
    suspend fun getHospitalOrderList(
        @Query("status") status: String,
    ) : List<HospitalOrderDetailResponse>
}