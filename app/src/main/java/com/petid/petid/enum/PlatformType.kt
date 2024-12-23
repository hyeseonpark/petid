package com.petid.petid.enum

enum class PlatformType(val value: String) {
    naver("naver"),
    google("google"),
    kakao("kakao");

    companion object {
        fun fromValue(value: String?): PlatformType? {
            return entries.find { it.value == value }
        }
    }
}
