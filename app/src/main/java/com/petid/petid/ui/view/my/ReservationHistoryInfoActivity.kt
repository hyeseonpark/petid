package com.petid.petid.ui.view.my

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.petid.petid.R
import com.petid.petid.common.Constants.EXTRA_HOSPITAL_ID
import com.petid.petid.databinding.ActivityReservationHistoryInfoBinding
import com.petid.petid.type.ReservationStatus
import com.petid.petid.ui.component.CustomDialogCommon
import com.petid.petid.ui.state.CommonUIState
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.ui.view.hospital.HospitalReservationActivity
import com.petid.petid.ui.view.my.adapter.HospitalReservationListAdapter
import com.petid.petid.util.collectLatestFlow
import com.petid.petid.util.showErrorMessage
import com.petid.petid.viewmodel.hospital.ReservationHistoryInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReservationHistoryInfoActivity : BaseActivity() {
    private lateinit var binding: ActivityReservationHistoryInfoBinding
    private val viewModel: ReservationHistoryInfoViewModel by viewModels()

    private lateinit var cancelDialog : CustomDialogCommon

    private lateinit var hospitalReservationListAdapter : HospitalReservationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationHistoryInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        setupToolbar(
            toolbar = findViewById(R.id.toolbar),
            showBackButton = true,
            title = resources.getString(R.string.reservation_history_title)
        )
        initComponent()

        observeReservationHospitalListState()
        observeCancelHospitalReservation()
        viewModel.getHospitalReservationHistoryListApiState()
    }

    private fun initComponent() {

        // adapter 초기화
        hospitalReservationListAdapter =
            HospitalReservationListAdapter(applicationContext) { id, hospitalId, status ->
                when(status) {
                    ReservationStatus.CONFIRMED.name -> cancelDialog(id)
                    ReservationStatus.PENDING.name -> cancelDialog(id)
                    ReservationStatus.CANCELLED.name -> goHospitalDetailActivity(hospitalId)
                    ReservationStatus.COMPLETED.name -> goHospitalDetailActivity(hospitalId)
                }
            }

        with(binding.recyclerviewHospitalReservationList) {
            layoutManager = LinearLayoutManager(applicationContext)
            addItemDecoration(
                DividerItemDecoration(applicationContext, LinearLayout.VERTICAL)
            )

            adapter = hospitalReservationListAdapter
        }
    }

    /**
     * 예약 목록 api observer
     */
    private fun observeReservationHospitalListState() {
        viewModel.hospitalReservationHistoryListApiState.collectLatestFlow(this) { result ->
            if (result !is CommonUIState.Loading)
                hideLoading()

            when (result) {
                is CommonUIState.Success -> {
                    val reservationList = result.data

                    when(reservationList.isNotEmpty()) {
                        true -> {
                            hospitalReservationListAdapter.submitList(reservationList)
                            isDataAvailable(true)
                        }
                        false -> isDataAvailable(false)
                    }
                }
                is CommonUIState.Error -> {
                    showErrorMessage(result.message.toString())
                    isDataAvailable(false)
                }
                is CommonUIState.Loading -> {
                    showLoading()
                    isDataAvailable(false)
                }
                is CommonUIState.Init -> {}
            }
        }
    }

    /**
     * 예약 목록 데이터 여부에 따른 화면 전환
     */
    private fun isDataAvailable(boolean: Boolean) {
        when(boolean) {
            true -> {
                if (binding.recyclerviewHospitalReservationList.visibility != View.VISIBLE) {
                    binding.recyclerviewHospitalReservationList.visibility = View.VISIBLE
                    binding.textViewNoData.visibility = View.GONE
                }
            }
            false -> {
                if (binding.textViewNoData.visibility != View.VISIBLE) {
                    binding.textViewNoData.visibility = View.VISIBLE
                    binding.recyclerviewHospitalReservationList.visibility = View.GONE
                }
            }
        }
    }

    /**
     * 예약취소 observer
     */
    private fun observeCancelHospitalReservation() {
        lifecycleScope.launch {
            viewModel.cancelHospitalReservationApiState.collectLatest { result ->
                if (result !is CommonUIState.Loading)
                    hideLoading()

                when (result) {
                    is CommonUIState.Success -> {
                        viewModel.getHospitalReservationHistoryListApiState()
                        cancelDialog.dismiss()
                    }
                    is CommonUIState.Error -> showErrorMessage(result.message.toString())
                    is CommonUIState.Loading -> showLoading()
                    is CommonUIState.Init -> {}
                }
            }
        }
    }

    /**
     * 예약 취소 dialog
     */
    private fun cancelDialog(id: Long) {
        cancelDialog = CustomDialogCommon(
            getString(R.string.cancel_reservation_dialog), {
                viewModel.cancelHospitalReservationApiState(id)
            })

        cancelDialog.show(this.supportFragmentManager, "CustomDialogCommon")
    }

    /**
     * HospitalDetailActivity 이동
     */
    private fun goHospitalDetailActivity(hospitalId: Long) {
        val intent = Intent(this, HospitalReservationActivity::class.java)
            .putExtra(EXTRA_HOSPITAL_ID, hospitalId)
        startActivity(intent)
    }
}