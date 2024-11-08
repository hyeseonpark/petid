package com.android.petid.ui.view.generate

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.databinding.FragmentUserInfoInputBinding

class UserInfoInputFragment : Fragment() {
    private lateinit var binding: FragmentUserInfoInputBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserInfoInputBinding.inflate(inflater)
        initComponent()

        return binding.root
    }

    fun initComponent() {
        with(binding) {
            editTextName.editText.addTextChangedListener(object : TextWatcher{

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (editTextName.editText.length() > 0) {
                        buttonNext.disable = false
                    }
                }
            })

            buttonNext.button.setOnClickListener{
                val nameValue = editTextName.editText.text.toString()
//            val regidencyValue = editTextResidencyNumber.text.toString() + editTextResidencyNumber2.text.toString()
                val phoneValue = editTextPhone.editText.text.toString()
                val addressValue = editTextAddress.editText.text.toString()

                findNavController().navigate(R.id.action_userInfoInputFragment_to_petInfoInputFragment)
            }
        }
    }
}