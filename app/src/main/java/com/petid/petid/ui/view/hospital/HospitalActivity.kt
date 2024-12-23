package com.petid.petid.ui.view.hospital

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.petid.domain.entity.HospitalEntity
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
            initData()
            setContentView(it.root)
        }
    }

    private fun initData() {
        viewModel.hospitalDetail = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("hospitalDetail", HospitalEntity::class.java)!!
        } else {
            (intent.getParcelableExtra("hospitalDetail") as? HospitalEntity)!!
        }
    }

}