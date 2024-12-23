package com.petid.petid.ui.view.deleted

import android.os.Bundle
import com.petid.petid.R
import com.petid.petid.databinding.ActivityGenerateCompleteBinding
import com.petid.petid.ui.view.common.BaseActivity

class GenerateCompleteActivity : BaseActivity() {
    private lateinit var binding: ActivityGenerateCompleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        setupToolbar(
            toolbar = findViewById(R.id.toolbar),
            showBackButton = true,
        )
    }
}