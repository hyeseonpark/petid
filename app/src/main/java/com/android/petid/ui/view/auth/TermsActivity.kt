package com.android.petid.ui.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.petid.R
import com.android.petid.databinding.ActivityTermsBinding
import com.android.petid.util.setStyleSpan
import com.android.petid.enum.PlatformType
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.common.BaseActivity
import com.android.petid.viewmodel.auth.TermsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TermsActivity : BaseActivity() {
    private lateinit var binding: ActivityTermsBinding
    private val viewModel: TermsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initComponent()
        setupJoinObservers()
    }

    private fun initComponent() {
        binding.textViewTitle.text =
            setStyleSpan(applicationContext, binding.textViewTitle.text.toString(),
                resources.getString(R.string.terms_activity_title_span), R.color.petid_clear_blue)

        binding.checkBoxAll.setOnClickListener {
            if (binding.checkBoxAll.isChecked) {
                binding.checkboxTermsAgree.isChecked = true
                binding.checkBoxPersonalInfoAgree.isChecked = true
                binding.checkboxAdsAgree.isChecked = true
            } else {
                binding.checkboxTermsAgree.isChecked = false
                binding.checkBoxPersonalInfoAgree.isChecked = false
                binding.checkboxAdsAgree.isChecked = false

            }
        }

        binding.checkboxTermsAgree.setOnCheckedChangeListener{ _ , isChecked ->
            allChecked()
            buttonEnable()
        }
        binding.checkBoxPersonalInfoAgree.setOnCheckedChangeListener{ _ , isChecked ->
            allChecked()
            buttonEnable()
        }
        binding.checkboxAdsAgree.setOnCheckedChangeListener{ _ , isChecked ->
            allChecked()
        }

        binding.buttonNext.button.setOnClickListener{
            doJoin()
        }
    }

    /**
     * 모두 동의 체크 박스
     */
    private fun allChecked() {
        if (binding.checkboxTermsAgree.isChecked &&
            binding.checkBoxPersonalInfoAgree.isChecked  &&
            binding.checkboxAdsAgree.isChecked ) {
            binding.checkBoxAll.isChecked = true
        } else {
            binding.checkBoxAll.isChecked = false
        }
    }

    /**
     * 개인약관 동의에 따른 버튼 활성화
     */
    private fun buttonEnable() {
        if (binding.checkboxTermsAgree.isChecked &&
            binding.checkBoxPersonalInfoAgree.isChecked) {
            binding.buttonNext.disable = false
        } else {
            binding.buttonNext.disable = true
        }
    }

    /**
     *
     */
    private fun setupJoinObservers() {
        lifecycleScope.launch {
            viewModel.apiState.collect { state ->
                when (state) {
                    is CommonApiState.Loading -> {
                        // 로딩 중 UI 표시
                    }
                    is CommonApiState.Success -> {
                        val intent = Intent(this@TermsActivity, SignupCompleteActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is CommonApiState.Error -> {
                        // 오류 처리
//                        Toast.makeText(this@TermsActivity, "오류 발생: ${state.message}", Toast.LENGTH_SHORT).show()
                    }
                    is CommonApiState.Init -> {}
                }
            }
        }
    }

    /**
     * 회원가입 api
     */
    private fun doJoin() {
        val platformString = intent.getStringExtra("platform")
        val platform = PlatformType.fromValue(platformString)
        val sub = intent.getStringExtra("sub")
        val fcmToken = intent.getStringExtra("fcmToken")

        if (platform != null && sub != null && fcmToken != null) {
            viewModel.join(platform, sub, fcmToken, binding.checkboxAdsAgree.isChecked)
        }
    }
}
// u-vjpFMMHtac_2Qd42Su3PqRpTNo9jr1AAAAAQo8JB8AAAGRtkcrK7fuZLkpz6yP