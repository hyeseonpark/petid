package com.android.petid.ui.view.hospital

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.domain.entity.HospitalEntity
import com.android.petid.databinding.ActivityHospitalBinding
import com.android.petid.viewmodel.hospital.HospitalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HospitalActivity : AppCompatActivity() {
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