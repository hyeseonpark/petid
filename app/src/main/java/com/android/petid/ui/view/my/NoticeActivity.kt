package com.android.petid.ui.view.my

import android.os.Bundle
import com.android.petid.databinding.ActivityNoticeBinding
import com.android.petid.ui.view.common.BaseActivity

class NoticeActivity : BaseActivity() {
    private lateinit var binding: ActivityNoticeBinding
    private val TAG = "NoticeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)

        initComponent()
        setContentView(binding.root)
    }

    private fun initComponent() {

    }
}