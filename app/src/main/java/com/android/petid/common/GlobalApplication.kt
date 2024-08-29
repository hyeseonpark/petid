package com.android.petid.common

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.android.petid.R
import com.kakao.sdk.common.KakaoSdk
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        // Kakao Sdk 초기화
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))

        Logger.addLogAdapter(AndroidLogAdapter())
    }
}