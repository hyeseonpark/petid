package com.petid.petid

import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.petid.petid.util.PreferencesControl
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kakao.sdk.common.KakaoSdk
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class GlobalApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        preferencesControl = PreferencesControl(appInstance)

        CoroutineScope(Dispatchers.Default).launch {
            // Kakao Sdk 초기화
            KakaoSdk.init(appInstance, BuildConfig.KAKAO_NATIVE_APP_KEY)

            Logger.addLogAdapter(AndroidLogAdapter())

            FirebaseApp.initializeApp(appInstance).apply {
                // Crashlytics 기본 설정
                if (BuildConfig.DEBUG) {
                    FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
                } else {
                    FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
                }
            }
        }
    }

    companion object {
        private lateinit var appInstance: GlobalApplication
        fun getGlobalContext() = appInstance
        private lateinit var preferencesControl: PreferencesControl
        fun getPreferencesControl() = preferencesControl
    }

}