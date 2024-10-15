package com.android.petid.ui.view.my

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.petid.R
import com.android.petid.databinding.ActivityMyPetInfoBinding
import com.android.petid.databinding.ActivityReservationHistoryInfoBinding
import com.android.petid.viewmodel.auth.TermsViewModel
import com.android.petid.viewmodel.hospital.ReservationHistoryInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReservationHistoryInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationHistoryInfoBinding
    private val viewModel: ReservationHistoryInfoViewModel by viewModels()

    private val TAG = "ReservationHistoryInfoActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationHistoryInfoBinding.inflate(layoutInflater)

        initComponent()
        setContentView(binding.root)
    }

    private fun initComponent() {

    }
}