package com.android.petid.ui.view.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.android.petid.R
import com.android.petid.common.FragmentInflate
import com.android.petid.util.ProgressDialogUtil
import com.android.petid.util.hideLoadingDialog
import com.android.petid.util.showLoadingDialog

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

    protected fun setupToolbar(
        toolbar: Toolbar,
        title: String? = null,
        showBackButton: Boolean = false,
        showUpdateButton: Boolean = false,
        onBackClick: (() -> Unit)? = null,
        onUpdateClick: (() -> Unit)? = null
    ) {
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolbar)

            supportActionBar?.setDisplayShowTitleEnabled(false)

            with(binding.root) {
                findViewById<ImageButton>(R.id.btnBack)?.apply {
                    visibility = if (showBackButton) View.VISIBLE else View.GONE
                    setOnClickListener {
                        onBackClick?.invoke() ?: findNavController().navigateUp()
                    }
                }

                findViewById<TextView>(R.id.tvTitle)?.apply {
                    visibility = if (title != null) View.VISIBLE else View.GONE
                    text = title
                }

                findViewById<TextView>(R.id.btnRight)?.apply {
                    visibility = if (showUpdateButton) View.VISIBLE else View.GONE
                    setOnClickListener { onUpdateClick?.invoke() }
                }
            }
        }
    }

    /**
     * show loading dialog
     */
    fun showLoading() {
        _binding?.root?.showLoadingDialog(requireContext())
    }

    /**
     * hide loading dialog
     */
    fun hideLoading() {
        _binding?.root?.hideLoadingDialog()
    }
}
