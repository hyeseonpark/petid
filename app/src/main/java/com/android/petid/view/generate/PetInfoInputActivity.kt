package com.android.petid.view.generate

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.R
import com.android.petid.databinding.ActivityPetInfoInputBinding
import com.android.petid.common.getCurrentDate
import com.android.petid.common.setStyleSpan

class PetInfoInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetInfoInputBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetInfoInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewTitle.text =
            setStyleSpan(applicationContext, binding.textViewTitle.text.toString(),
                resources.getString(R.string.pet_info_input_activity_title_span), R.color.petid_clear_blue)

        binding.editTextName.editText.addTextChangedListener(object : TextWatcher {

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


        binding.editTextBirth.editText.setOnTouchListener{ v, event ->
            var textBirth = binding.editTextBirth.editText.text.toString()
            if(textBirth.isEmpty()) {
                textBirth = getCurrentDate()
            }

            val etSDateStr = textBirth.replace("[^0-9]".toRegex(), "")
            DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    binding.editTextBirth.editText.setText(
                        "${year}.${monthOfYear + 1}.${dayOfMonth}"
                    )
                },
                etSDateStr.substring(0, 4).toInt(),
                etSDateStr.substring(4, 6).toInt() - 1,
                etSDateStr.substring(6, 8).toInt()
            ).show()

            false
        }

        binding.buttonNext.button.setOnClickListener{
            var nextIntent : Intent
            nextIntent = Intent(this, PetPhotoActivity::class.java)
//            if(intent.getBooleanExtra("generated", false)) {
//                nextIntent = Intent(this, CompleteCardActivity::class.java)
//            } else {
//                nextIntent = Intent(this, PetPhotoActivity::class.java)
//            }
            startActivity(nextIntent)
        }
    }
}