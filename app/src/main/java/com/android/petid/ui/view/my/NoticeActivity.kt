package com.android.petid.ui.view.my

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.petid.R
import com.android.petid.databinding.ActivityMyPetInfoBinding
import com.android.petid.databinding.ActivityNoticeBinding

class NoticeActivity : AppCompatActivity() {
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