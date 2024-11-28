package com.android.petid.ui.view.hospital

import android.os.Bundle
import com.android.petid.databinding.ActivityCompleteReservationBinding
import com.android.petid.ui.view.common.BaseActivity

class CompleteReservationActivity : BaseActivity() {
    private lateinit var binding: ActivityCompleteReservationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}