package com.android.petid.ui.view.common

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val cutout = insets.displayCutout
            if (cutout != null) {
                val topInset = cutout.safeInsetTop
                view.setPadding(0, topInset, 0, 0)
            }
            insets
        }
    }
}