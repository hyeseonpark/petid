package com.petid.petid.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Throttle first
 * @param intervalTime 입력된 시간동안 이벤트를 진입시키지 않음
 */
fun <T> Flow<T>.throttleFirst(
    intervalTime: Long = 300L
) : Flow<T> = flow{
    var throttleTime = 0L
    collect { upFlow ->
        val currentTime = System.currentTimeMillis()
        if ((currentTime - throttleTime) > intervalTime) {
            throttleTime = currentTime
            emit(upFlow)
        }
    }
}