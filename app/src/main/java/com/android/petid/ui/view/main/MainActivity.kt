package com.android.petid.ui.view.main

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.fragment.app.Fragment
import com.android.petid.databinding.ActivityMainBinding
import com.android.petid.ui.view.home.HomeMainFragment
import com.android.petid.ui.view.hospital.HospitalMainFragment
import com.android.petid.ui.view.my.MyMainFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기 프래그먼트 설정
        val initialFragment = HomeMainFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(binding.frameLayout.id, initialFragment)
            commit()
        }

        binding.tabLayoutMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                var selectedFragment = when (tab?.position) {
                    0 -> HomeMainFragment()
                    1 -> HospitalMainFragment()
                    2 -> com.android.petid.ui.view.blog.BlogMainFragment()
                    3 -> MyMainFragment()
                    else -> HomeMainFragment()
                }
                selectedFragment.let {
                    supportFragmentManager.beginTransaction().replace(
                        binding.frameLayout.id,
                        it
                    ).commit()
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })



//        val pagerAdapter = FragmentAdapter(supportFragmentManager)
//
//        val pager = binding.frameLayout
//        pager.adapter = pagerAdapter
//
//        val tab = findViewById<TabLayout>(R.id.tab)
//        tab.setupWithViewPager(pager)

        // setContentView하기 전에 installSplashScreen() 필수
//        splashScreen = installSplashScreen()
////        startSplash()
//        setContentView(binding.root)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
    // splash의 애니메이션 설정
    private fun startSplash() {
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 5f, 1f)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 5f, 1f)

            ObjectAnimator.ofPropertyValuesHolder(splashScreenView.iconView, scaleX, scaleY).run {
                interpolator = AnticipateInterpolator()
                duration = 1000L
                doOnEnd {
                    splashScreenView.remove()
                }
                start()
            }
        }
    }

//    private fun setFragment(frag : Fragment) {  //2번
//        supportFragmentManager.commit {
//            replace(R.id.frameLayout, frag)
//            setReorderingAllowed(true)
//            addToBackStack("")
//        }
//    }
}