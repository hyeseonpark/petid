package com.android.petid.view.hospital

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.petid.R
import com.android.petid.databinding.FragmentHospitalBinding
import com.android.petid.databinding.FragmentMyBinding

class HospitalFragment : Fragment() {
    lateinit var binding: FragmentHospitalBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHospitalBinding.inflate(inflater)
        return binding.root
    }

    fun newInstant() : HospitalFragment
    {
        val args = Bundle()
        val frag = HospitalFragment()
        frag.arguments = args
        return frag
    }
}