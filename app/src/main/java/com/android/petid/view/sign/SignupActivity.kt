package com.android.petid.view.sign

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editTextName.editText.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.editTextName.editText.length() > 0) {
                    binding.buttonNext.disable = false
                }
            }
        })

        binding.buttonNext.button.setOnClickListener{
            val nameValue = binding.editTextName.editText.text.toString()
            val regidencyValue = binding.editTextResidencyNumber.text.toString() + binding.editTextResidencyNumber2.text.toString()
            val phoneValue = binding.editTextPhone.editText.text.toString()
            val addressValue = binding.editTextAddress.editText.text.toString()

            val intent = Intent(this, SignupCompleteActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}