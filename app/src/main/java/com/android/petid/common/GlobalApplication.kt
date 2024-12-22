package com.android.petid.common

import androidx.multidex.MultiDexApplication
import com.android.petid.BuildConfig
import com.android.petid.util.PreferencesControl
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kakao.sdk.common.KakaoSdk
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        appInstance = this
        preferencesControl = PreferencesControl(getGlobalContext())

        // Kakao Sdk 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

        Logger.addLogAdapter(AndroidLogAdapter())

        // Crashlytics 기본 설정
        if (BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
        } else {
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
        }
    }

    companion object {
        private lateinit var appInstance: GlobalApplication
        private lateinit var preferencesControl : PreferencesControl
        fun getGlobalContext() = appInstance
        fun getPreferencesControl() = preferencesControl
    }

}