package com.petid.petid.ui.view.home

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.petid.petid.R
import com.petid.petid.databinding.ActivityNotificationBinding
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.util.TAG
import com.petid.petid.viewmodel.home.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationActivity : BaseActivity() {
    private lateinit var binding : ActivityNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

    override fun onStart() {
        super.onStart()

        setupToolbar(
            toolbar = findViewById(R.id.toolbar),
            showBackButton = true,
            title = resources.getString(R.string.notification_title)
        )
        observeNoticationListState()
        viewModel.getNotificationList()
    }

    /**
     *
     */
    private fun observeNoticationListState() {
        lifecycleScope.launch {
            viewModel.notificationListState.collectLatest { result ->
               if (result != CommonApiState.Loading)
                    hideLoading()

                when(result) {
                    is CommonApiState.Success -> {
                        Log.d(TAG, result.data.size.toString())
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, result.message.toString())
                    }
                    CommonApiState.Loading -> showLoading()
                    CommonApiState.Init -> {}
                }
            }
        }
    }
}