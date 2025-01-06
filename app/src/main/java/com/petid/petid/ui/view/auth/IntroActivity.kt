package com.petid.petid.ui.view.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.petid.petid.R
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.databinding.ActivityIntroBinding
import com.petid.petid.ui.view.common.BaseActivity

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding
    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter

    private var dotsCount = 0
    private lateinit var dots: Array<ImageView?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        initComponent()
        setContentView(binding.root)
    }

    private fun initComponent() {
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, lifecycle)

        with(binding) {
            buttonStart.setOnClickListener{
                val target = Intent(getGlobalContext(), SocialAuthActivity::class.java)
                startActivity(target)
                finish()
            }

            viewPager.apply {
                setAdapter(mSectionsPagerAdapter)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        setCurrentPosition(position)
                    }
                })
            }
        }
        setUiPageViewController()
    }


    /**
     * ViewPage Indicator 설정
     */
    private fun setUiPageViewController() {
        dotsCount = mSectionsPagerAdapter.itemCount
        dots = arrayOfNulls(dotsCount)

        val dotsLp =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(13, 0, 13, 0)
            }

        for (i in 0 until dotsCount) {
            dots[i] = ImageView(getGlobalContext()).apply {
                setImageResource(R.drawable.ic_indicator_off)
                setLayoutParams(dotsLp)
            }
            binding.layoutViewPagerDots.addView(dots[i])
        }

        dots[0]?.setImageResource(R.drawable.ic_indicator_on)
    }

    /**
     * ViewPage Indicator 표시
     * @param index 현재 위치
     */
    private fun setCurrentPosition(index: Int) {
        for (i in 0 until dotsCount) {
            dots[i]?.setImageResource(R.drawable.ic_indicator_off)
        }

        dots[index]?.setImageResource(R.drawable.ic_indicator_on)

        binding.buttonStart.visibility = when(index) {
            dotsCount - 1 -> View.VISIBLE
            else -> View.GONE
        }
    }

    /**
     *
     */
    class SectionsPagerAdapter(fm: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc) {
        private val pagerCount = 3

        override fun getItemCount(): Int {
            return pagerCount
        }

        override fun createFragment(position: Int): Fragment {
            return IntroFragment.newInstance(position)
        }
    }
}