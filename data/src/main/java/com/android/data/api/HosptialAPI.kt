package com.android.data.api

import com.android.data.model.HospitalResponse
import retrofit2.http.Field
import retrofit2.http.GET
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
}