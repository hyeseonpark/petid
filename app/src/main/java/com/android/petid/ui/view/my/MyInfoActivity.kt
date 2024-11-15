package com.android.petid.ui.view.my

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.petid.R
import com.android.petid.databinding.ActivityMyInfoBinding
import com.android.petid.databinding.FragmentMyInfoDetailBinding
import com.android.petid.viewmodel.my.MyInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyInfoBinding
    private val viewModel: MyInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater).also{
            setContentView(it.root)
        }
    }
}