package com.android.petid.ui.view.main

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.android.petid.R
import com.android.petid.databinding.ActivityMainBinding
import com.android.petid.util.hideKeyboardAndClearFocus
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var navMainFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_layout_main) as NavHostFragment
        val navController = navMainFragment.findNavController()
        binding.bottomNavigation.setupWithNavController(navController)
    }

    /**
     * EditText 바깥 터치 시 키보드 숨기기
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboardAndClearFocus()
        return super.dispatchTouchEvent(ev)
    }
}