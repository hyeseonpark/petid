package com.petid.petid.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

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

/**
 *  LifecycleOwner가 Started 상태일 때 collectLatest 실행
 */
fun <T> Flow<T>.collectLatestFlow(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    collector: suspend (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(state) {
            collectLatest(collector)
        }
    }
}