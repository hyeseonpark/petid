package com.android.petid.ui.view.generate

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.petid.R
import com.android.petid.databinding.ActivityCheckingInfoBinding
import com.android.petid.databinding.ActivityPetInfoInputBinding

class CheckingInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckingInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckingInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonNext.button.setOnClickListener{
            val intent = Intent(this, SignatureActivity::class.java)
            startActivity(intent)
        }
    }
}