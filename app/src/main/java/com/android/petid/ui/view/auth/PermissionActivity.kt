package com.android.petid.ui.view.auth

import android.graphics.Color
import android.os.Bundle
import com.android.petid.R
import com.android.petid.databinding.ActivityPermissionBinding
import com.android.petid.ui.view.common.BaseActivity
import com.android.petid.util.Utils.setStyleSpan

class PermissionActivity : BaseActivity() {
    private lateinit var binding: ActivityPermissionBinding
    private val TAG = "PermissionActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponent()
    }

    private fun initComponent() {
        binding.permissionTitle1.text =
            setStyleSpan(applicationContext, binding.permissionTitle1.text.toString(),
                resources.getString(R.string.permission_title_1_span), Color.parseColor("#E54747"));

        binding.buttonConfirm.button.setOnClickListener{

        }
    }

}