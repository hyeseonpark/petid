package com.android.petid.ui.view.my

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.android.petid.R
import com.android.petid.databinding.ActivityHospitalDetailBinding
import com.android.petid.databinding.ActivityMyInfoBinding
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.generate.UserInfoInputActivity
import com.android.petid.viewmodel.blog.ContentDetailViewModel
import com.android.petid.viewmodel.my.MyInfoViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 마이페이지 메인 > 내 정보
 */

@AndroidEntryPoint
class MyInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyInfoBinding
    private val viewModel: MyInfoViewModel by viewModels()

    private val TAG = "MyInfoActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)

        initComponent()
        observeGetMemberInfoState()
        viewModel.getMemberInfo()

        setContentView(binding.root)
    }

    private fun initComponent() {
        binding.topBar.buttonBack.setOnClickListener { finish() }
        binding.topBar.textViewButton.setOnClickListener{
            val intent = Intent(this, MyInfoUpdateActivity::class.java)
            startActivity(intent)
        }

        // Glide.with(this).load(R.drawable.myimage).circleCrop().into(binding.imageViewProfile);
    }


    private fun observeGetMemberInfoState() {
        lifecycleScope.launch {
            viewModel.getMemberInfoResult.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        with(result.data) {

                            // Glide.with(applicationContext).load(image).into(binding.imageViewProfile)

                            binding.textViewName.text = name
                            // binding.textViewBirth.text =
                            binding.textViewPhoneNumber.text = phone
                            binding.textViewAddress.text = address
                        }
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                    }
                }
            }
        }
    }
}