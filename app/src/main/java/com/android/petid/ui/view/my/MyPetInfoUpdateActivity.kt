package com.android.petid.ui.view.my

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.petid.R
import com.android.petid.databinding.ActivityMyPetInfoBinding
import com.android.petid.databinding.ActivityMyPetInfoUpdateBinding

class MyPetInfoUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPetInfoUpdateBinding
    private val TAG = "MyPetInfoActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPetInfoUpdateBinding.inflate(layoutInflater)

        initComponent()
        setContentView(binding.root)
    }

    private fun initComponent() {

    }
}