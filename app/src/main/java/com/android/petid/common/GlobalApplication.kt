package com.android.petid.common

import androidx.multidex.MultiDexApplication
import com.android.petid.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        appInstance = this

        // Kakao Sdk 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

        Logger.addLogAdapter(AndroidLogAdapter())
    }

    companion object {
        private lateinit var appInstance: GlobalApplication
        fun getGlobalContext() = appInstance
    }

}