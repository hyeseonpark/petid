package com.android.petid.ui.view.common

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.android.petid.R

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

    protected fun setupToolbar(
        toolbar: Toolbar,
        title: String? = null,
        showBackButton: Boolean = false,
        showUpdateButton: Boolean = false,
        onBackClick: (() -> Unit)? = null,
        onUpdateClick: (() -> Unit)? = null
    ) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        findViewById<ImageButton>(R.id.btnBack)?.apply {
            visibility = if (showBackButton) View.VISIBLE else View.GONE
            setOnClickListener {
                onBackClick?.invoke() ?: finish()
            }
        }

        findViewById<TextView>(R.id.tvTitle)?.apply {
            visibility = if (title != null) View.VISIBLE else View.GONE
            text = title
        }

        findViewById<TextView>(R.id.btnRight)?.apply {
            visibility = if (showUpdateButton) View.VISIBLE else View.GONE
            setOnClickListener { onUpdateClick?.invoke() }
        }

    }
}