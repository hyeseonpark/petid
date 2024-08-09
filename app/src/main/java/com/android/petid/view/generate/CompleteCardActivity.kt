package com.android.petid.view.generate

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import com.android.petid.R
import com.android.petid.databinding.ActivityCompleteCardBinding
import com.android.petid.databinding.ActivityGenerateCompleteBinding
import com.android.petid.ui.CustomDialogCommon
import com.android.petid.ui.CustomDialogInterface

class CompleteCardActivity : AppCompatActivity(), CustomDialogInterface {
    private lateinit var binding: ActivityCompleteCardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonComplete.button.setOnClickListener{
            val dialog = CustomDialogCommon(this, "패키지를 삭제하시겠습니까?")
            // 알림창이 띄워져있는 동안 배경 클릭 막기
            dialog.isCancelable = false
            dialog.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show(this.supportFragmentManager, "CustomDialogCommon")
        }
    }

    override fun onYesButtonClick() {
    }
}