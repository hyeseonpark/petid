package com.android.petid.view.hospital

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.petid.R
import com.android.petid.databinding.ActivityHospitalDetailBinding
import com.android.petid.databinding.ActivityPermissionBinding
import com.android.petid.view.sign.TermsActivity
import java.util.Calendar

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