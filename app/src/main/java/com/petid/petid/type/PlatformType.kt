package com.petid.petid.type

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
