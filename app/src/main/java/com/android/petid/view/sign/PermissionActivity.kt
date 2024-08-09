package com.android.petid.view.sign

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.R
import com.android.petid.common.Constants.SHARED_VALUE_IS_FIRST
import com.android.petid.common.PreferencesControl
import com.android.petid.common.setStyleSpan
import com.android.petid.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {
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