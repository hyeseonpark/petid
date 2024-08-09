package com.android.petid.view.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.petid.R
import com.android.petid.databinding.FragmentHomeBinding
import com.android.petid.view.generate.PetInfoInputActivity

class HomeFragment : Fragment() {
    lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)

        binding.cardAnimation.setOnClickListener{
            val intent = Intent(activity, PetInfoInputActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }

    fun newInstant() : HomeFragment
    {
        val args = Bundle()
        val frag = HomeFragment()
        frag.arguments = args
        return frag
    }
}
