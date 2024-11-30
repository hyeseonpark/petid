package com.android.petid.ui.view.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.android.petid.databinding.FragmentAddressSearchBinding


class AddressSearchFragment : BaseFragment<FragmentAddressSearchBinding>(FragmentAddressSearchBinding::inflate) {
    companion object {
        fun newInstance()= AddressSearchFragment()
        const val url = "http://yourpet-id.com/address"
        const val daumPostCode = "javascript:sample2_execDaumPostcode();"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressSearchBinding.inflate(layoutInflater)
        initComponent()

        return binding.root
    }

    fun initComponent() {
        with(binding) {
            initWebView()
            buttonBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebView() {
        with(binding.webView) {
            settings.javaScriptEnabled = true
            addJavascriptInterface(MyJavaScriptInterface(), "Android")
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    loadUrl(daumPostCode)
                }
            }
            loadUrl(Companion.url)
        }
    }

    inner class MyJavaScriptInterface {
        @JavascriptInterface
        @Suppress("unused")
        fun processDATA(data: String?) {
            activity?.runOnUiThread {
                setFragmentResult(BundleKeys.KEY_ADDRESS, bundleOf(BundleKeys.KEY_ADDRESS to data))
                findNavController().popBackStack()
            }
        }
    }
}