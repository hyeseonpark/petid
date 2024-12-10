package com.android.petid.ui.view.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.android.petid.R
import com.android.petid.common.Constants
import com.android.petid.common.GlobalApplication.Companion.getGlobalContext
import com.android.petid.databinding.ActivitySplashBinding
import com.android.petid.ui.view.common.BaseActivity
import com.android.petid.ui.view.main.MainActivity
import com.android.petid.util.PreferencesControl

/**
 * 스플래시 화면
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var preferencesControl : PreferencesControl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        //installSplashScreen()
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    fun init() {
        preferencesControl = PreferencesControl(getGlobalContext())

        val isFirstRun =
            preferencesControl.getBooleanValue(Constants.SHARED_VALUE_IS_FIRST_RUN, true)

        val refreshToken =
            preferencesControl.getStringValue(Constants.SHARED_VALUE_REFRESH_TOKEN)

        val nextActivity = when {
            isFirstRun -> PermissionActivity::class.java
            refreshToken != "" -> MainActivity::class.java
            else -> SocialAuthActivity::class.java
        }

        val intent = Intent(getGlobalContext(), nextActivity)
        startActivity(intent)
        finish()
    }
}