package com.android.petid.ui.view.generate

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.databinding.FragmentPetInfoInputBinding
import com.android.petid.util.getCurrentDate
import com.android.petid.util.setStyleSpan

class PetInfoInputFragment : Fragment() {
    private lateinit var binding: FragmentPetInfoInputBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPetInfoInputBinding.inflate(layoutInflater)
        initComponent()

        return binding.root
    }

    fun initComponent() {
        binding.textViewTitle.text =
            setStyleSpan(requireContext(), binding.textViewTitle.text.toString(),
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
                requireContext() ,
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
            findNavController().navigate(R.id.action_petInfoInputFragment_to_petPhotoFragment)
        }
    }
}