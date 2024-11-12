package com.android.petid.common

object Constants {
    const val SHARED_VALUE_IS_FIRST = "isFirst"
    const val REQUEST_STORAGE_PERMISSION = 1
    const val SHARED_VALUE_ACCESS_TOKEN: String = "accessToken"
    const val SHARED_VALUE_REFRESH_TOKEN: String = "refreshToken"

    const val LOCATION_SIDO_TYPE = 0
    const val LOCATION_SIGUNGU_TYPE = 1
    const val LOCATION_EUPMUNDONG_TYPE = 2

    const val BANNER_TYPE_MAIN = "main"
    const val BANNER_TYPE_CONTENT = "content"

    private const val DAY_SUN = "SUNDAY"
    private const val DAY_MON = "MONDAY"
    private const val DAY_TUES = "TUESDAY"
    private const val DAY_WED = "WEDNESDAY"
    private const val DAY_THUR = "THURSDAY"
    private const val DAY_FRI = "FRIDAY"
    private const val DAY_SAT = "SATURDAY"
    val DAYS_OF_WEEK = arrayOf(DAY_SUN, DAY_MON, DAY_TUES, DAY_WED, DAY_THUR, DAY_FRI, DAY_SAT)

    // TODO enum -> constants
}