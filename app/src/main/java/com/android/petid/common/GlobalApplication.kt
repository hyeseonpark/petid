package com.android.petid.common

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.android.petid.BuildConfig
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
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

        Logger.addLogAdapter(AndroidLogAdapter())
    }
}