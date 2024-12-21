package com.android.petid.ui.view.home

import android.os.Bundle
import com.android.petid.R
import com.android.petid.databinding.ActivityNotificationBinding
import com.android.petid.ui.view.common.BaseActivity

class NotificationActivity : BaseActivity() {
    private lateinit var binding : ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

    override fun onStart() {
        super.onStart()

        setupToolbar(
            toolbar = findViewById(R.id.toolbar),
            showBackButton = true,
            title = resources.getString(R.string.notification_title)
        )
    }
}