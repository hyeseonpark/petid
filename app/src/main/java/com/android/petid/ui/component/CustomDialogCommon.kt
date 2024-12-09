package com.android.petid.ui.component

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.android.petid.R
import com.android.petid.common.GlobalApplication.Companion.getGlobalContext
import com.android.petid.databinding.DialogCommonBinding

class CustomDialogCommon(
    private val title: String,
    private val yesButtonClick: (() -> Any?)? = null,
    private val noButtonClick: (() -> Any)? = null,
    private val isSingleButton: Boolean? = null,
    private val singleButtonText: String? = getGlobalContext().getString(R.string.yes),
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

        with(binding) {
            textViewTitle.text = title

            buttonNo.setOnClickListener {
                noButtonClick?.invoke()
                dismiss()
            }

            buttonYes.setOnClickListener {
                when(yesButtonClick) {
                    null -> dismiss()
                    else -> yesButtonClick.invoke()
                }
            }

            if (isSingleButton == true) {
                buttonNo.visibility = View.GONE
                buttonYes.text = singleButtonText
            }

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
