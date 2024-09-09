package com.android.petid.ui.view.hospital

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.domain.entity.HospitalEntity
import com.android.petid.databinding.ActivityHospitalDetailBinding

class HospitalDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHospitalDetailBinding
    private val TAG = "HospitalDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalDetailBinding.inflate(layoutInflater)

        initComponent()
        setContentView(binding.root)
    }

    private fun initComponent() {
        val hospitalDetail = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("hospitalDetail", HospitalEntity::class.java)
        } else {
            intent.getParcelableExtra("hospitalDetail") as?  HospitalEntity
        }

        binding.textViewTitle.text = hospitalDetail!!.name
        binding.textViewVet.text = hospitalDetail!!.vet
        binding.textViewTime.text = hospitalDetail!!.hours
        binding.textViewTel.text = hospitalDetail!!.tel

        binding.buttonReserve.button.setOnClickListener{
            val intent = Intent(this, ReservationCalendarActivity::class.java)
            startActivity(intent)
        }
    }
}