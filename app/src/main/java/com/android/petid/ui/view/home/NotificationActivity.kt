package com.android.petid.ui.view.home

import android.os.Bundle
import com.android.petid.databinding.ActivityNotificationBinding
import com.android.petid.ui.view.common.BaseActivity

class NotificationActivity : BaseActivity() {
    private lateinit var binding : ActivityNotificationBinding

    private val TAG = "NotificationFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }
}