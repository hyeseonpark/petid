package com.android.data.api

import com.android.data.dto.response.MemberInfoResponse
import com.android.data.dto.response.UpdateMemberInfoResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MemberAPI {

    /**
     * 1-1.1 광고성 정보 수신동의 정책 수정 API
     */
    @FormUrlEncoded
    @POST("/v1/member/policy")
    suspend fun updateMemberPolicy(
        @Field("ad") ad: Boolean
    ): Boolean

    /**
     * 1-1.2 회원 정보 업데이트 API
     */
    @FormUrlEncoded
    @POST("/v1/member/auth")
    suspend fun updateMemberInfo(
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("addressDetails") addressDetails: String,
        @Field("phone") phone: String
    ): UpdateMemberInfoResponse

    /**
     * 1-1.3 회원 정보 저장 여부 조회 API
     */
    @GET("/v1/member/auth")
    suspend fun checkMemberInfoSaved(): Boolean

    /**
     * 1-1.4 회원 정보 조회 API
     */
    @GET("/v1/member")
    suspend fun getMemberInfo(): MemberInfoResponse

    /**
     * 1-1.5 회원 프로필사진 업로드 API
     */
    @POST("/v1/member/images/presigned-url")
    suspend fun uploadProfileImage(
        @Body imagePath: String
    ): String

    /**
     * 1-1.6 회원 프로필사진 조회 API
     */
    @GET("/v1/member/images/presigned-url")
    suspend fun getProfileImageUrl(
        @Query("path") imagePath: String
    ): String
}