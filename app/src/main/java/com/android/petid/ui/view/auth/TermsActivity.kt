package com.android.petid.ui.view.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.R
import com.android.petid.databinding.ActivityTermsBinding
import com.android.petid.common.setStyleSpan

class TermsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsBinding.inflate(layoutInflater)

        binding.textViewTitle.text =
            setStyleSpan(applicationContext, binding.textViewTitle.text.toString(),
            resources.getString(R.string.terms_activity_title_span), R.color.petid_clear_blue)

//        val content = binding.textViewTitle.text.toString()
//        val spannableString = SpannableString(content)
//
//        val word = String.format(resources.getString(R.string.terms_activity_title_span))
//        val start = content.indexOf(word)
//        val end = start + word.length
//
//        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(applicationContext, R.color.petid_clear_blue)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        binding.textViewTitle.text = spannableString

        setContentView(binding.root)

        binding.checkBoxAll.setOnClickListener {
            if (binding.checkBoxAll.isChecked) {
                binding.checkboxTermsAgree.isChecked = true
                binding.checkBoxPersonalInfoAgree.isChecked = true
                binding.checkboxAdsAgree.isChecked = true
            } else {
                binding.checkboxTermsAgree.isChecked = false
                binding.checkBoxPersonalInfoAgree.isChecked = false
                binding.checkboxAdsAgree.isChecked = false

            }
        }
        binding.checkboxTermsAgree.setOnCheckedChangeListener{ _ , isChecked ->
            allChecked()
            buttonEnable()
        }
        binding.checkBoxPersonalInfoAgree.setOnCheckedChangeListener{ _ , isChecked ->
            allChecked()
            buttonEnable()
        }
        binding.checkboxAdsAgree.setOnCheckedChangeListener{ _ , isChecked ->
            allChecked()
        }

        binding.buttonNext.button.setOnClickListener{
            val intent = Intent(this, SignupCompleteActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun allChecked() {
        if (binding.checkboxTermsAgree.isChecked &&
            binding.checkBoxPersonalInfoAgree.isChecked  &&
            binding.checkboxAdsAgree.isChecked ) {
            binding.checkBoxAll.isChecked = true
        } else {
            binding.checkBoxAll.isChecked = false
        }
    }

    private fun buttonEnable() {
        if (binding.checkboxTermsAgree.isChecked &&
            binding.checkBoxPersonalInfoAgree.isChecked) {
            binding.buttonNext.disable = false
        } else {
            binding.buttonNext.disable = true
        }
    }
}