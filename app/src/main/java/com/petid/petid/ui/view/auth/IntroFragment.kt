package com.petid.petid.ui.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.petid.petid.R
import com.petid.petid.databinding.FragmentIntroBinding
import com.petid.petid.ui.view.common.BaseFragment

class IntroFragment : BaseFragment<FragmentIntroBinding>(FragmentIntroBinding::inflate) {
    private var mIndex = 0

    private val guideImageIds = arrayOf(
        R.drawable.img_intro_1,
        R.drawable.img_intro_2,
        R.drawable.img_intro_3
    )

    private val guideTitleIds = arrayOf(
        R.string.intro_1_title,
        R.string.intro_2_title,
        R.string.intro_3_title
    )

    private val guideDescIds = arrayOf(
        R.string.intro_1_desc,
        R.string.intro_2_desc,
        R.string.intro_3_desc
    )

    companion object{
        fun newInstance(position: Int): IntroFragment {
            val fragment = IntroFragment()
            with(Bundle()){
                putInt("index", position)
                fragment.arguments = this
            }
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIndex = requireArguments().getInt("index")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroBinding.inflate(inflater)
        initComponent()
        return binding.root
    }

    fun initComponent() {
        with(binding) {
            textViewTitle.text = getString(guideTitleIds[mIndex])
            textViewDesc.text = getString(guideDescIds[mIndex])
            when(mIndex) {
                2 -> { // Design Issue
                    imageViewIntro.visibility = View.GONE
                    imageViewIntro3.visibility = View.VISIBLE
                }
                else -> {
                    imageViewIntro.setImageResource(guideImageIds[mIndex])
                    imageViewIntro.visibility = View.VISIBLE
                    imageViewIntro3.visibility = View.GONE
                }
            }
        }
    }
}