package com.android.data.dto.request

/**
 * @param name 신청자 이름
 * @param address 신청자 주소
 * @param addressDetails 신청자 주소 상세
 * @param phone 신청자 연락처
 */
data class PetProposerRequest(
    val name: String,
    val address: String,
    val addressDetails: String,
    val phone: String,
)