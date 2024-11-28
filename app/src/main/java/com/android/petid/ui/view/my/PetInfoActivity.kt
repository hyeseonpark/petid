package com.android.petid.ui.view.my

import android.os.Bundle
import com.android.petid.databinding.ActivityPetInfoBinding
import com.android.petid.ui.view.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PetInfoActivity : BaseActivity() {
    private lateinit var binding: ActivityPetInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetInfoBinding.inflate(layoutInflater).also{
            setContentView(it.root)
        }
    }
}