package com.android.domain.entity

data class HospitalOrderDetailEntity (
    val id: Int,
    val uid: String,
    val hospitalId: Int,
    val date: Long, // Unix timestamp
    val status: String
)