package com.android.petid.view.generate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.databinding.ActivityGenerateCompleteBinding

class GenerateCompleteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenerateCompleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}