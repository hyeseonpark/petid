package com.petid.petid.ui.view.main

import android.os.Bundle
import android.view.MotionEvent
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.petid.petid.R
import com.petid.petid.databinding.ActivityMainBinding
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.util.hideKeyboardAndClearFocus
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navMainFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_layout_main) as NavHostFragment
        val navController = navMainFragment.findNavController()

        binding.bottomNavigation.let {
            it.setupWithNavController(navController)
            disableBottomNavigationViewTooltips(it)
        }
    }

    /**
     * EditText 바깥 터치 시 키보드 숨기기
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboardAndClearFocus()
        return super.dispatchTouchEvent(ev)
    }

    /**
     * tooltip 제거
     */
    private fun disableBottomNavigationViewTooltips(bottomNavigationView: BottomNavigationView) {
        val menuView = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        for (i in 0 until menuView.childCount) {
            val itemView = menuView.getChildAt(i)
            itemView.setOnLongClickListener { true }
            itemView.tooltipText = null
        }
    }
}