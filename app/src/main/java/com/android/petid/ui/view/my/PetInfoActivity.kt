package com.android.petid.ui.view.my

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.databinding.ActivityPetInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PetInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetInfoBinding.inflate(layoutInflater).also{
            setContentView(it.root)
        }
    }
}