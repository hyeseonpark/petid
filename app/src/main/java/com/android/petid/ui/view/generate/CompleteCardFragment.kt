package com.android.petid.ui.view.generate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.android.petid.R
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.databinding.FragmentCompleteCardBinding
import com.android.petid.ui.view.main.MainActivity

class CompleteCardFragment: BaseFragment<FragmentCompleteCardBinding>(FragmentCompleteCardBinding::inflate) {

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompleteCardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
            onBackClick = { activity?.finish() }
        )
        initComponent()
    }

    fun initComponent() {
        binding.buttonComplete.setOnClickListener{
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