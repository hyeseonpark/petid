package com.android.petid.ui.view.my

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.petid.R
import com.android.petid.databinding.ActivityReservationHistoryInfoBinding
import com.android.petid.enum.ReservationStatus
import com.android.petid.ui.component.CustomDialogCommon
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.hospital.HospitalActivity
import com.android.petid.ui.view.my.adapter.HospitalReservationListAdapter
import com.android.petid.viewmodel.hospital.ReservationHistoryInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReservationHistoryInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationHistoryInfoBinding
    private val viewModel: ReservationHistoryInfoViewModel by viewModels()

    private val TAG = "ReservationHistoryInfoActivity"

    private lateinit var cancelDialog : CustomDialogCommon

    private lateinit var hospitalReservationListAdapter : HospitalReservationListAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationHistoryInfoBinding.inflate(layoutInflater)

        initComponent()

        observeReservationHospitalListState()
        observeCancelHospitalReservation()
        viewModel.getHospitalReservationHistoryListApiState()

        setContentView(binding.root)
    }

    private fun initComponent() {

        // adapter 초기화
        hospitalReservationListAdapter =
            HospitalReservationListAdapter(applicationContext) { id, status ->
                when(status) {
                    ReservationStatus.CONFIRMED.name -> cancelDialog(id)
                    ReservationStatus.PENDING.name -> cancelDialog(id)
                    ReservationStatus.CANCELLED.name -> goHospitalDetailActivity(id)
                    ReservationStatus.COMPLETED.name -> goHospitalDetailActivity(id)
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
        lifecycleScope.launch {
            viewModel.hospitalReservationHistoryListApiState.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        val reservationList = result.data

                        when(reservationList.isNotEmpty()) {
                            true -> {
                                hospitalReservationListAdapter.submitList(reservationList)
                                isDataAvailable(true)
                            }
                            false -> isDataAvailable(false)
                        }
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                        isDataAvailable(false)
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                        isDataAvailable(false)
                    }
                }
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
                when (result) {
                    is CommonApiState.Success -> {
                        viewModel.getHospitalReservationHistoryListApiState()
                        cancelDialog.dismiss()
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                    }
                }
            }
        }
    }

    /**
     * 예약 취소 dialog
     */
    private fun cancelDialog(id: Int) {
        cancelDialog = CustomDialogCommon(
            getString(R.string.cancel_reservation_dialog), {
                viewModel.cancelHospitalReservationApiState(id)
            })

        cancelDialog.show(this.supportFragmentManager, "CustomDialogCommon")
    }

    /**
     * HospitalDetailActivity 이동
     */
    private fun goHospitalDetailActivity(id: Int) {
        val intent = Intent(this, HospitalActivity::class.java)
            .putExtra("hospitalDetail", id)
        startActivity(intent)
    }
}