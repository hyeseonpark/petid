package com.android.petid.ui.view.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.common.Constants
import com.android.petid.common.GlobalApplication.Companion.getGlobalContext
import com.android.petid.common.GlobalApplication.Companion.getPreferencesControl
import com.android.petid.databinding.ActivitySplashBinding
import com.android.petid.ui.view.main.MainActivity

/**
 * 스플래시 화면
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(InitRunnable(), 1500L)
    }

    inner class InitRunnable : Runnable{
        override fun run() {
            val isFirstRun =
                getPreferencesControl().getBooleanValue(Constants.SHARED_VALUE_IS_FIRST_RUN, true)

            val refreshToken =
                getPreferencesControl().getStringValue(Constants.SHARED_VALUE_REFRESH_TOKEN)

            val nextActivity = when {
                isFirstRun -> PermissionActivity::class.java
                refreshToken != "" -> MainActivity::class.java
                else -> SocialAuthActivity::class.java
            }

            val target = Intent(getGlobalContext(), nextActivity).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(target)
            finish()
        }
    }
}