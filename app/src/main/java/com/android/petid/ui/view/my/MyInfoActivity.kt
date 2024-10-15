package com.android.petid.ui.view.my

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.petid.R
import com.android.petid.databinding.ActivityHospitalDetailBinding
import com.android.petid.databinding.ActivityMyInfoBinding
import com.android.petid.ui.view.generate.UserInfoInputActivity

/**
 * 마이페이지 메인 > 내 정보
 */
class MyInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyInfoBinding
    private val TAG = "MyInfoActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)

        initComponent()
        setContentView(binding.root)
    }

    private fun initComponent() {
        binding.topBar.buttonBack.setOnClickListener { finish() }
        binding.topBar.textViewButton.setOnClickListener{
            val intent = Intent(this, MyInfoUpdateActivity::class.java)
            startActivity(intent)
        }

        // Glide.with(this).load(R.drawable.myimage).circleCrop().into(binding.imageViewProfile);
    }
}