package com.android.petid.ui.view.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.petid.R
import com.android.petid.databinding.ActivityTermsBinding
import com.android.petid.util.Utils.setStyleSpan
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
        with(binding) {
            textViewTitle.text =
                setStyleSpan(applicationContext, textViewTitle.text.toString(),
                    resources.getString(R.string.terms_activity_title_span), R.color.petid_clear_blue)

            checkBoxAll.setOnClickListener {
                if (checkBoxAll.isChecked) {
                    checkboxTermsAgree.isChecked = true
                    checkBoxPersonalInfoAgree.isChecked = true
                    checkboxAdsAgree.isChecked = true
                } else {
                    checkboxTermsAgree.isChecked = false
                    checkBoxPersonalInfoAgree.isChecked = false
                    checkboxAdsAgree.isChecked = false

                }
            }

            checkboxTermsAgree.setOnCheckedChangeListener{ _ , isChecked ->
                allChecked()
                buttonEnable()
            }
            checkBoxPersonalInfoAgree.setOnCheckedChangeListener{ _ , isChecked ->
                allChecked()
                buttonEnable()
            }
            checkboxAdsAgree.setOnCheckedChangeListener{ _ , isChecked ->
                allChecked()
            }

            buttonNext.setOnClickListener{
                doJoin()
            }
        }
    }

    /**
     * 모두 동의 체크 박스
     */
    private fun allChecked() {
        with(binding) {
            checkBoxAll.isChecked = checkboxTermsAgree.isChecked &&
                    checkBoxPersonalInfoAgree.isChecked &&
                    checkboxAdsAgree.isChecked
        }
    }

    /**
     * 개인약관 동의에 따른 버튼 활성화
     */
    private fun buttonEnable() {
        with(binding) {
            buttonNext.isEnabled = checkboxTermsAgree.isChecked &&
                    checkBoxPersonalInfoAgree.isChecked
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