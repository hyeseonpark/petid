package com.petid.data.dto.request

import com.petid.domain.entity.PetProposerRequestEntity

/**
 * @param name 신청자 이름
 * @param address 신청자 주소
 * @param addressDetails 신청자 주소 상세
 * @param rra Resident Registration Act, 주민등록 상 주소(신청자 주소와 같을 경우 같은 값)
 * @param rraDetails 주민등록 상 주소 상세
 * @param phone 신청자 연락처
 */
data class PetProposerRequest(
    val name: String,
    val address: String,
    val addressDetails: String,
    val rra: String,
    val rraDetails: String,
    val phone: String,
)

fun PetProposerRequest.toDomain() = PetProposerRequestEntity(
    name = name,
    address = address,
    addressDetails = addressDetails,
    rra = rra,
    rraDetails = rraDetails,
    phone = phone,
)