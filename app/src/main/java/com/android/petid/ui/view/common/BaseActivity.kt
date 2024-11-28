package com.android.petid.ui.view.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.R

open class BaseActivity : AppCompatActivity() {
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