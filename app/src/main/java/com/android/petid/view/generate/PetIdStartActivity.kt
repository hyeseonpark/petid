package com.android.petid.view.generate

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.petid.R
import com.android.petid.databinding.ActivityPetIdStartBinding
import com.android.petid.databinding.ActivitySignupCompleteBinding
import com.android.petid.view.sign.TermsActivity

class PetIdStartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetIdStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetIdStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStart.button.setOnClickListener{
            val intent = Intent(this, PetInfoInputActivity::class.java)
            startActivity(intent)
        }

        binding.textViewGenerated.setOnClickListener{
            val intent = Intent(this, PetInfoInputActivity::class.java)
            intent.putExtra("generated", true)
            startActivity(intent)
        }
    }
}