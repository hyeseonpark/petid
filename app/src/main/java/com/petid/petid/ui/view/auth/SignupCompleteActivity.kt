package com.petid.petid.ui.view.auth

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.databinding.ActivitySignupCompleteBinding
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.ui.view.main.MainActivity
import com.petid.petid.util.throttleFirst
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

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
            buttonConfirm
                .clicks()
                .throttleFirst()
                .onEach {
                    val target = Intent(getGlobalContext(), MainActivity::class.java)
                    startActivity(target)
                    finish()
                }
                .launchIn(lifecycleScope)

            imageButtonClose
                .clicks()
                .throttleFirst()
                .onEach {
                    val target = Intent(getGlobalContext(), MainActivity::class.java)
                    startActivity(target)
                    finish()
                }
                .launchIn(lifecycleScope)
        }
    }
}