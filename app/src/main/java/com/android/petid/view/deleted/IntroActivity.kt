package com.android.petid.view.deleted

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.databinding.ActivityIntroBinding
import com.android.petid.view.sign.TermsActivity

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)

        binding.buttonNext.button.setOnClickListener{
            val intentTerms = Intent(this, TermsActivity::class.java)
            startActivity(intentTerms)
            finish()
        }
        setContentView(binding.root)
    }
}