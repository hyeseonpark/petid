package com.android.data.api

import com.android.data.model.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface LocationAPI {

    /**
     * 시도 조회
     * 모든 시도를 조회합니다.
     */
    @GET("v1/location")
    suspend fun getSidoList(): List<LocationResponse>

    /**
     * 시군구 조회
     * 특정 시도에 속한 시군구를 조회합니다.
     *
     * @param sidoId 시도 ID
     */
    @GET("v1/location/sido/{sidoId}/sigungu")
    suspend fun getSigunguList(
        @Path("sidoId") sidoId: Int
    ): List<LocationResponse>

    /**
     * 읍면동 조회
     * 특정 시군구에 속한 읍면동을 조회합니다.
     *
     * @param sigunguId 시군구 ID
     */
    @GET("v1/location/sigungu/{sigunguId}/eupmundong")
    suspend fun getEupmyeondongList(
        @Path("sigunguId") sigunguId: Int
    ): List<LocationResponse>
}