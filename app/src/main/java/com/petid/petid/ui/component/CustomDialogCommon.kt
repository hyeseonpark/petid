package com.petid.petid.ui.component

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.petid.petid.R
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.databinding.DialogCommonBinding

class CustomDialogCommon(
    private val title: String,
    private val yesButtonClick: (() -> Any?)? = null,
    private val noButtonClick: (() -> Any)? = null,
    private val isSingleButton: Boolean? = null,
    private val singleButtonText: String? = getGlobalContext().getString(R.string.yes),
    private val boldTitle: String? = null,
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

            if (boldTitle != null) {
                textViewTitleBold.apply {
                    visibility = View.VISIBLE
                    text = boldTitle
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
            return CustomDialogCommon(
                title = title,
                yesButtonClick = yesButtonClick,
                noButtonClick = noButtonClick
            )
        }
    }
}
