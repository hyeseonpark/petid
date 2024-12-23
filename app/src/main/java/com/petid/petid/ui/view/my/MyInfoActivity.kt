package com.petid.petid.ui.view.my

import android.os.Bundle
import androidx.activity.viewModels
import com.petid.petid.databinding.ActivityMyInfoBinding
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.viewmodel.my.MyInfoViewModel
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