package com.android.petid.ui.view.generate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.databinding.ActivityPetIdStartBinding

class PetIdStartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetIdStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetIdStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener{
            val intent = Intent(this, UserInfoInputActivity::class.java)
            startActivity(intent)
        }

        binding.button2.setOnClickListener{
            val intent = Intent(this, UserInfoInputActivity::class.java)
            startActivity(intent)
        }

        binding.button3.setOnClickListener{
            val intent = Intent(this, UserInfoInputActivity::class.java)
            startActivity(intent)
        }
    }
}