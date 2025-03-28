package com.petid.petid.ui.view.hospital


import android.os.Bundle
import androidx.activity.viewModels
import com.petid.petid.R
import com.petid.petid.common.Constants.EXTRA_HOSPITAL_ID
import com.petid.petid.databinding.ActivityHospitalBinding
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.util.showMessage
import com.petid.petid.viewmodel.hospital.HospitalReservationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HospitalReservationActivity : BaseActivity() {
    private lateinit var binding : ActivityHospitalBinding
    private val viewModel: HospitalReservationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHospitalBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        val hospitalId = intent.getIntExtra(EXTRA_HOSPITAL_ID, -1)
        if (hospitalId == -1) {
            showMessage(getString(R.string.retry_message))
            finish()
            return
        }
    }

}
