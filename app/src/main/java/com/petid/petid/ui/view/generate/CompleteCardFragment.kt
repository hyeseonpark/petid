package com.petid.petid.ui.view.generate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.petid.petid.R
import com.petid.petid.databinding.FragmentCompleteCardBinding
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.util.throttleFirst
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class CompleteCardFragment: BaseFragment<FragmentCompleteCardBinding>(FragmentCompleteCardBinding::inflate) {

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompleteCardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
            onBackClick = { activity?.finish() }
        )
        initComponent()
    }

    fun initComponent() {
        binding.buttonComplete
            .clicks()
            .throttleFirst()
            .onEach {
                activity?.finish()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}