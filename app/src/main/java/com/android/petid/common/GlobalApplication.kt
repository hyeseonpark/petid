package com.android.petid.common

import android.app.Application
import com.android.petid.R
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao Sdk 초기화
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))
    }
}