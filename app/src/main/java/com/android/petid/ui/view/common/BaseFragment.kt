package com.android.petid.ui.view.common

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.android.petid.common.FragmentInflate
import com.android.petid.util.ProgressDialogUtil

abstract class BaseFragment<VB: ViewBinding>(
    private val inflate: FragmentInflate<VB>
): Fragment() {
    var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showLoading() {
        _binding?.also { ProgressDialogUtil.show(requireContext()) }
    }

    fun hideLoading() {
        ProgressDialogUtil.cancel()
    }
}