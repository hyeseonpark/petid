package com.android.petid.enum

enum class PlatformType(val value: String) {
    naver("naver"),
    google("google"),
    kakao("kakao");

    companion object {
        fun fromValue(value: String?): PlatformType? {
            return values().find { it.value == value }
        }
    }
}
