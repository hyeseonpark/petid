package com.petid.petid.ui.view.common

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
import com.petid.petid.R
import com.petid.petid.util.FragmentInflate
import com.petid.petid.util.hideLoadingDialog
import com.petid.petid.util.showLoadingDialog

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

    /**
     * Configures the provided toolbar as the support action bar and sets up its UI elements.
     *
     * This method assigns the [toolbar] as the activity's support action bar (if the activity is an AppCompatActivity)
     * and customizes its layout by managing the visibility and click actions of the back button, title, and update button.
     * The back button (identified by R.id.btnBack) becomes visible if [showBackButton] is true and triggers either the
     * provided [onBackClick] lambda or a default navigation-up action if none is provided. The title, shown in the view
     * with R.id.tvTitle, is visible when [title] is non-null, and the update button (R.id.btnRight) is displayed when
     * [showUpdateButton] is true and triggers [onUpdateClick] when clicked.
     *
     * @param toolbar The Toolbar to set as the support action bar.
     * @param title Optional title text to display; if null, the title view is hidden.
     * @param showBackButton If true, shows the back button.
     * @param showUpdateButton If true, shows the update button.
     * @param onBackClick Optional lambda executed when the back button is clicked. Defaults to navigating up if not provided.
     * @param onUpdateClick Optional lambda executed when the update button is clicked.
     */
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
     * Updates the toolbar title displayed in the fragment.
     *
     * This method retrieves the TextView with the ID `tvTitle` from the fragment's root view and updates its text
     * to the provided [title]. The TextView is made visible when a non-null title is provided; otherwise, it is hidden.
     * The update is applied only if the fragment's activity is an instance of AppCompatActivity.
     *
     * @param title The new title to display in the toolbar.
     */
    protected fun setupTitle(title: String) {
        (activity as? AppCompatActivity)?.apply {
            with(binding.root) {
                findViewById<TextView>(R.id.tvTitle)?.apply {
                    visibility = if (title != null) View.VISIBLE else View.GONE
                    text = title
                }
            }
        }
    }

    /**
     * show loading dialog
     */
    fun showLoading() {
        _binding?.root?.showLoadingDialog(requireActivity())
    }

    /**
     * hide loading dialog
     */
    fun hideLoading() {
        _binding?.root?.hideLoadingDialog()
    }
}
