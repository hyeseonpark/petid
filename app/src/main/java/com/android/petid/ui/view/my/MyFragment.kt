package com.android.petid.ui.view.my

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.petid.R
import com.android.petid.databinding.FragmentBlogBinding
import com.android.petid.databinding.FragmentMyBinding

class MyFragment : Fragment() {
    lateinit var binding: FragmentMyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBinding.inflate(inflater)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_my, container, false)
    }

    fun newInstant() : MyFragment
    {
        val args = Bundle()
        val frag = MyFragment()
        frag.arguments = args
        return frag
    }
}