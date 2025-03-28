package com.petid.petid.ui.view.hospital


import android.os.Bundle
import androidx.activity.viewModels
import com.petid.petid.databinding.ActivityHospitalBinding
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.viewmodel.hospital.HospitalReservationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HospitalReservationActivity : BaseActivity() {
    private lateinit var binding : ActivityHospitalBinding
    private val viewModel: HospitalReservationViewModel by viewModels()

    /**
     * Initializes the activity by inflating the layout using View Binding and setting the content view.
     *
     * @param savedInstanceState Bundle containing the activity's previously saved state, or null if this is a new instance.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHospitalBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

}
