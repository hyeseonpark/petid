package com.android.petid.ui.view.my

import android.os.Bundle
import com.android.petid.R
import com.android.petid.databinding.ActivityNoticeBinding
import com.android.petid.ui.view.common.BaseActivity

class NoticeActivity : BaseActivity() {
    private lateinit var binding: ActivityNoticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        setupToolbar(
            toolbar = findViewById(R.id.toolbar),
            showBackButton = true,
            title = resources.getString(R.string.notice_title)
        )
    }
}