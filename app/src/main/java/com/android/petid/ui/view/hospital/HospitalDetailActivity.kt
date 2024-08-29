package com.android.petid.ui.view.hospital

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.databinding.ActivityHospitalDetailBinding

class HospitalDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHospitalDetailBinding
    private val TAG = "HospitalDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponent()
    }

    private fun initComponent() {
        binding.buttonReserve.button.setOnClickListener{
            val intent = Intent(this, ReservationCalendarActivity::class.java)
            startActivity(intent)
        }
    }
}