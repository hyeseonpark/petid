package com.android.petid.ui.view.generate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.android.petid.R
import com.android.petid.databinding.ActivityGeneratePetidMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeneratePetidMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGeneratePetidMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGeneratePetidMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        supportFragmentManager.findFragmentById(R.id.fragment_layout_generate) as NavHostFragment
    }
}