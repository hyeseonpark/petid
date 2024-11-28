package com.android.petid.ui.view.deleted

import android.content.Intent
import android.os.Bundle
import com.android.petid.databinding.ActivityIntroBinding
import com.android.petid.ui.view.auth.TermsActivity
import com.android.petid.ui.view.common.BaseActivity

class IntroActivity : BaseActivity() {
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