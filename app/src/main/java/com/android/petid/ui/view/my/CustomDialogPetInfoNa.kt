package com.android.petid.ui.view.my

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.android.petid.R
import com.android.petid.databinding.DialogPetinfoNaBinding

class CustomDialogPetInfoNa: DialogFragment() {
    private var _binding: DialogPetinfoNaBinding? = null
    private val binding get() = _binding!!

    private var currentStep = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPetinfoNaBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initComponent()
        return binding.root
    }

    fun initComponent() {
        with(binding) {
            buttonSingle.setOnClickListener {
                when(currentStep) {
                    0 -> {
                        textViewDesc11.visibility = View.GONE
                        textViewDesc12.visibility = View.GONE
                        textViewDesc21.visibility = View.VISIBLE
                        buttonSingle.text = resources.getString(R.string.search_hospital)
                        currentStep = 1
                    }
                    1 -> dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): CustomDialogPetInfoNa {
            return CustomDialogPetInfoNa()
        }
    }
}