package com.android.petid.ui.component

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.android.petid.databinding.DialogCommonBinding

class CustomDialogCommon(
    private val title: String,
    private val yesButtonClick: (() -> Unit?)? = null,
    private val noButtonClick: (() -> Unit)? = null
) : DialogFragment() {
    private var _binding: DialogCommonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCommonBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.textViewTitle.text = title

        binding.buttonNo.setOnClickListener {
            noButtonClick?.invoke()
            dismiss()
        }

        binding.buttonYes.setOnClickListener {
            yesButtonClick?.invoke()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            title: String,
            yesButtonClick: () -> Unit,
            noButtonClick: (() -> Unit)? = null
        ): CustomDialogCommon {
            return CustomDialogCommon(title, yesButtonClick, noButtonClick)
        }
    }
}
