package com.android.petid.view.hospital

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.petid.R
import com.android.petid.databinding.ActivityCompleteReservationBinding
import com.android.petid.databinding.ActivityWatingReservationBinding

class WatingReservationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWatingReservationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatingReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}