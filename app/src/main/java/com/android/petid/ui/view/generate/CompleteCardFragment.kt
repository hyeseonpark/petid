package com.android.petid.ui.view.generate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.petid.databinding.FragmentCompleteCardBinding
import com.android.petid.ui.view.main.MainActivity

class CompleteCardFragment : Fragment() {
    private lateinit var binding: FragmentCompleteCardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompleteCardBinding.inflate(layoutInflater)
        initComponent()

        return binding.root
    }

    fun initComponent() {
        binding.buttonComplete.button.setOnClickListener{
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}

/*class CompleteCardActivity : AppCompatActivity(), CustomDialogInterface {
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
}*/