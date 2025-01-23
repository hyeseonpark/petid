package com.petid.petid.common

object Constants {
    const val NOTIFICATION_DATA = "notification_data"

    const val SHARED_VALUE_IS_FIRST_RUN = "isFirst"
    const val REQUEST_STORAGE_PERMISSION = 1

    const val SHARED_AUTH_PROVIDER = "authProvider"

    const val SHARED_PET_ID_VALUE = "petIdValue"
    const val SHARED_MEMBER_ID_VALUE = "memberIdValue"
    const val SHARED_PET_CHIP_TYPE = "petChipTypeValue"

    // extra name for commonInfoActivity
    val COMMON_CATEGORY_TYPE = "categoryType"

    // location type
    const val LOCATION_SIDO_TYPE = 0
    const val LOCATION_SIGUNGU_TYPE = 1
    const val LOCATION_EUPMUNDONG_TYPE = 2

    // banner type
    const val BANNER_TYPE_MAIN = "main"
    const val BANNER_TYPE_CONTENT = "content"

    // 요일
    private const val DAY_SUN = "SUNDAY"
    private const val DAY_MON = "MONDAY"
    private const val DAY_TUES = "TUESDAY"
    private const val DAY_WED = "WEDNESDAY"
    private const val DAY_THUR = "THURSDAY"
    private const val DAY_FRI = "FRIDAY"
    private const val DAY_SAT = "SATURDAY"
    val DAYS_OF_WEEK = arrayOf(DAY_SUN, DAY_MON, DAY_TUES, DAY_WED, DAY_THUR, DAY_FRI, DAY_SAT)

    // chip type
    private const val NA = "NA" // 칩 없음
    private const val EXTERNAL = "EXTERNAL" // 외장칩
    private const val INTERNAL = "INTERNAL" // 내장칩
    val CHIP_TYPE = arrayOf(NA, EXTERNAL, INTERNAL)

    // 사진 저장 경로
    private const val MEMBER = "member/profile/" // 멤버 프로필 이미지
    private const val SIGN = "member/sign/" // 사인 이미지
    private const val PET = "pet/profile/" // 애완동물 프로필 이미지
    val PHOTO_PATHS = arrayOf(MEMBER, SIGN, PET)

    // TODO enum -> constants
    // content category type
    private const val ALL = "ALL"
    private const val RECOMMENDED = "RECOMMENDED"
    private const val ABOUTPET = "ABOUTPET"
    private const val TIPS = "TIPS"
    private const val VENUE = "VENUE"
    private const val SUPPORT = "SUPPORT"
    val CONTENT_CATEGORY_TYPE = arrayOf(ALL, RECOMMENDED, ABOUTPET, TIPS, VENUE, SUPPORT)

}