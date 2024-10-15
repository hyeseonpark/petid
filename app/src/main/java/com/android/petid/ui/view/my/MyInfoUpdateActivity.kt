package com.android.petid.ui.view.my

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.petid.R
import com.android.petid.databinding.ActivityMyInfoBinding
import com.android.petid.databinding.ActivityMyInfoUpdateBinding

/**
 * 마이페이지 메인 > 내 정보 > 내 정보 수정
 */
class MyInfoUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyInfoUpdateBinding
    private val TAG = "MyInfoUpdateActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoUpdateBinding.inflate(layoutInflater)

        initComponent()
        setContentView(binding.root)
    }

    private fun initComponent() {

    }
}