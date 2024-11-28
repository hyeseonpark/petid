package com.android.petid.ui.view.auth

import android.content.Intent
import android.os.Bundle
import com.android.petid.databinding.ActivitySignupCompleteBinding
import com.android.petid.ui.view.common.BaseActivity
import com.android.petid.ui.view.main.MainActivity

class SignupCompleteActivity : BaseActivity() {
    private lateinit var binding: ActivitySignupCompleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponent()
    }

    private fun initComponent() {
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