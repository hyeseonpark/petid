package com.petid.domain.entity

/**
 * @param name 신청자 이름
 * @param address 신청자 주소
 * @param addressDetails 신청자 주소 상세
 * @param rra Resident Registration Act, 주민등록 상 주소(신청자 주소와 같을 경우 같은 값)
 * @param rraDetails 주민등록 상 주소 상세
 * @param phone 신청자 연락처
 */
data class PetProposer(
    val name: String,
    val address: String,
    val addressDetails: String,
    val rra: String,
    val rraDetails: String,
    val phone: String,
)