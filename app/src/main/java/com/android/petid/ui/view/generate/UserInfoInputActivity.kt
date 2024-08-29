package com.android.petid.ui.view.generate

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.databinding.ActivityUserInfoInputBinding

class UserInfoInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoInputBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoInputBinding.inflate(layoutInflater)
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
//            val regidencyValue = binding.editTextResidencyNumber.text.toString() + binding.editTextResidencyNumber2.text.toString()
            val phoneValue = binding.editTextPhone.editText.text.toString()
            val addressValue = binding.editTextAddress.editText.text.toString()

            val intent = Intent(this, PetInfoInputActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}