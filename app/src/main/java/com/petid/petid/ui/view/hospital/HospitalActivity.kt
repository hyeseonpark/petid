package com.petid.petid.ui.view.hospital


import android.os.Bundle
import androidx.activity.viewModels
import com.petid.petid.databinding.ActivityHospitalBinding
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.viewmodel.hospital.HospitalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HospitalActivity : BaseActivity() {
    private lateinit var binding : ActivityHospitalBinding
    private val viewModel: HospitalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHospitalBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

}
