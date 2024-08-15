package com.android.petid.view.sign

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.petid.R
import com.android.petid.databinding.ActivityIntroBinding
import com.android.petid.databinding.ActivitySignupCompleteBinding
import com.android.petid.view.generate.PetIdStartActivity
import com.android.petid.view.generate.PetInfoInputActivity
import com.android.petid.view.main.MainActivity

class SignupCompleteActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupCompleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonConfirm.button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.imageButtonClose.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}