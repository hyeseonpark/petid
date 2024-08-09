package com.android.petid.view.blog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.petid.R
import com.android.petid.databinding.FragmentBlogBinding
import com.android.petid.databinding.FragmentHomeBinding

class BlogFragment : Fragment() {
    lateinit var binding: FragmentBlogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBlogBinding.inflate(inflater)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    fun newInstant() : BlogFragment
    {
        val args = Bundle()
        val frag = BlogFragment()
        frag.arguments = args
        return frag
    }
}