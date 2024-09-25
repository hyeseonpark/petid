package com.android.petid.ui.view.blog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.petid.databinding.FragmentBlogBinding

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

    fun newInstant() : com.android.petid.ui.view.blog.BlogFragment
    {
        val args = Bundle()
        val frag = com.android.petid.ui.view.blog.BlogFragment()
        frag.arguments = args
        return frag
    }
}