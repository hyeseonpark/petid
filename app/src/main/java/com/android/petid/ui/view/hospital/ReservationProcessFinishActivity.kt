package com.android.petid.ui.view.hospital

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.databinding.ActivityReservationProcessFinishBinding
import com.android.petid.ui.view.main.MainActivity

class ReservationProcessFinishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationProcessFinishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationProcessFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponent()
    }

    fun initComponent() {
        binding.buttonConfirm.button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}