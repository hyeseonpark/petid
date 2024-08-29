package com.android.petid.ui.view.hospital

data class HospitalItem(
    val id: Int,
    val imageUrl: String?, // 이미지 URL
    val address: String, // 병원 주소
    val name: String, // 병원 이름
    val hours: String?, // 운영 시간
    val tel: String, // 전화번호
    val vet: String // 수의사명
)
