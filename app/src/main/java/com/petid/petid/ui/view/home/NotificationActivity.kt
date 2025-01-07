package com.petid.petid.ui.view.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.petid.petid.R
import com.petid.petid.databinding.ActivityNotificationBinding
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.ui.view.home.adapter.NotificationListAdapter
import com.petid.petid.util.TAG
import com.petid.petid.viewmodel.home.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationActivity : BaseActivity() {
    private lateinit var binding : ActivityNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()

    private lateinit var notificationListAdapter: NotificationListAdapter

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
        observeNotificationListState()
        viewModel.getNotificationList()

        initComponent()
    }

    private fun initComponent() {

        // adapter 초기화
        notificationListAdapter =
            NotificationListAdapter() { id, status ->
                viewModel.markAsChecked(id)
            }

        with(binding.recyclerviewNotificationList) {
            layoutManager = LinearLayoutManager(applicationContext)
            addItemDecoration(DividerItemDecoration(applicationContext, LinearLayout.VERTICAL))

            adapter = notificationListAdapter
        }
    }

    /**
     * viewModel.notificationListState 상태값
     */
    private fun observeNotificationListState() {
        lifecycleScope.launch {
            viewModel.notificationListState.collectLatest { result ->
               if (result != CommonApiState.Loading)
                    hideLoading()

                when(result) {
                    is CommonApiState.Success -> {
                        notificationListAdapter.submitList(result.data)
                        isDataAvailable(true)
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, result.message.toString())
                        isDataAvailable(false)
                    }
                    CommonApiState.Loading -> showLoading()
                    CommonApiState.Init -> {}
                }
            }
        }
    }

    /**
     * 예약 목록 데이터 여부에 따른 화면 전환
     */
    private fun isDataAvailable(boolean: Boolean) {
        with(binding) {
            when(boolean) {
                true -> {
                    if (recyclerviewNotificationList.visibility != View.VISIBLE) {
                        recyclerviewNotificationList.visibility = View.VISIBLE
                        textViewNoData.visibility = View.GONE
                    }
                }
                false -> {
                    if (textViewNoData.visibility != View.VISIBLE) {
                        textViewNoData.visibility = View.VISIBLE
                        recyclerviewNotificationList.visibility = View.GONE
                    }
                }
            }
        }
    }
}