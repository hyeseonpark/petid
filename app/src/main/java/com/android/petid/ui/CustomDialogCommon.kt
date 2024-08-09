package com.android.petid.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.android.petid.databinding.DialogPetidGenerateCompleteBinding

class CustomDialogCommon(
    customDialogInterface: CustomDialogInterface,
    text: String
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogPetidGenerateCompleteBinding? = null
    private val binding get() = _binding!!

    private var customDialogInterface: CustomDialogInterface? = null

    private var text: String? = null
//    private var id: Int? = null

    init {
        this.text = text
//        this.id = id
        this.customDialogInterface = customDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPetidGenerateCompleteBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//        binding.confirmTextView.text = text

//        // 취소 버튼 클릭
//        binding.noButton.setOnClickListener {
//            dismiss()
//        }
//
//        // 확인 버튼 클릭
//        binding.yesButton.setOnClickListener {
//            this.confirmDialogInterface?.onYesButtonClick(id!!)
//            dismiss()
//        }

        binding.buttonSingle.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface CustomDialogInterface {
    fun onYesButtonClick()
}