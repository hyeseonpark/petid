package com.android.data.api

import com.android.data.dto.request.HospitalOrderRequest
import com.android.data.dto.response.HospitalOrderDetailResponse
import com.android.data.dto.response.HospitalOrderResponse
import com.android.data.dto.response.HospitalResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
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
     * 동물병원 예약 가능 시간 리스트 조회
     */
    @GET("/v1/hospital/order/time")
    suspend fun getHospitalOrderTimeList(
        @Query("hospitalId") hospitalId: Int,
        @Query("day") day: String,
        @Query("date") date: String // yyyy-mm-dd
    ) : List<String>

    /**
     * 동물병원 예약 내역 생성
     */
    /*@FormUrlEncoded
    @POST("/v1/hospital/order")
    suspend fun createHospitalOrder(
        @Field("hospitalId") hospitalId: Int,
        @Field("date") date: String // ISO-8601
    ) : HospitalOrderResponse*/

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
    @DELETE("/v1/hospital/order")
    suspend fun deleteHospitalOrder(
        @Field("orderId") orderId: Int,
    ) : Int

    /**
     * 동물병원 예약 목록 조회
     */
    @GET("/v1/hospital/order")
    suspend fun getHospitalOrderList(
        @Query("status") status: String,
    ) : List<HospitalOrderDetailResponse>
}