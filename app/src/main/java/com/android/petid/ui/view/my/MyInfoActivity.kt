package com.android.petid.ui.view.my

import android.os.Bundle
import androidx.activity.viewModels
import com.android.petid.databinding.ActivityMyInfoBinding
import com.android.petid.ui.view.common.BaseActivity
import com.android.petid.viewmodel.my.MyInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyInfoActivity : BaseActivity() {
    private lateinit var binding: ActivityMyInfoBinding
    private val viewModel: MyInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater).also{
            setContentView(it.root)
        }
    }
}