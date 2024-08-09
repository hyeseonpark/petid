package com.android.petid.view.generate

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.petid.R
import com.android.petid.databinding.ActivityPetInfoInputBinding
import com.android.petid.databinding.ActivityScannedInfoBinding

class ScannedInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScannedInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannedInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonNext.button.setOnClickListener{
            val intent = Intent(this, CheckingInfoActivity::class.java)
            startActivity(intent)
        }
    }
}