package com.petid.petid.ui.view.auth

import android.content.Intent
import android.os.Bundle
import com.petid.petid.common.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.databinding.ActivitySignupCompleteBinding
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.ui.view.main.MainActivity

class SignupCompleteActivity : BaseActivity() {
    private lateinit var binding: ActivitySignupCompleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponent()
    }

    private fun initComponent() {
        with(binding) {
            buttonConfirm.setOnClickListener{
                val target = Intent(getGlobalContext(), MainActivity::class.java)
                startActivity(target)
                finish()
            }

            imageButtonClose.setOnClickListener{
                val target = Intent(getGlobalContext(), MainActivity::class.java)
                startActivity(target)
                finish()
            }
        }
    }
}