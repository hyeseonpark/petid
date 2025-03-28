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

    /**
     * Lifecycle callback invoked when the activity becomes visible.
     *
     * Configures the toolbar with a back button and localized title, initializes UI components,
     * and starts observing state changes for both reservation history and reservation cancellation.
     */
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
    }

    /**
     * Initializes the hospital reservation list component.
     *
     * Sets up the adapter for displaying hospital reservations with a click listener that triggers:
     * - A cancellation dialog for reservations in CONFIRMED or PENDING status.
     * - Navigation to the hospital detail view for reservations in CANCELLED or COMPLETED status.
     *
     * Also configures the RecyclerView with a linear layout manager and a vertical divider decoration.
     */
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
     * Observes changes in the hospital reservation history API state and updates the UI accordingly.
     *
     * This method collects the latest state from the hospital reservation history flow and performs the following actions:
     * - When the state indicates success:
     *   - If reservations are available, it updates the adapter with the reservation list and sets the UI to reflect that data is available.
     *   - If the reservation list is empty, it marks the data as unavailable.
     * - When an error occurs, it displays an error message and marks data as unavailable.
     * - When loading, it shows a loading indicator and marks data as unavailable.
     *
     * Additionally, it hides any visible loading indicators when the state is not loading.
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
    private fun cancelDialog(id: Int) {
        cancelDialog = CustomDialogCommon(
            getString(R.string.cancel_reservation_dialog), {
                viewModel.cancelHospitalReservationApiState(id)
            })

        cancelDialog.show(this.supportFragmentManager, "CustomDialogCommon")
    }

    /**
     * Launches the HospitalReservationActivity to display detailed reservation information.
     *
     * Creates an intent that passes the hospital's unique identifier using the EXTRA_HOSPITAL_ID constant
     * and starts the activity.
     *
     * @param id The unique identifier for the selected hospital.
     */
    private fun goHospitalDetailActivity(id: Int) {
        val intent = Intent(this, HospitalReservationActivity::class.java)
            .putExtra(EXTRA_HOSPITAL_ID, id)
        startActivity(intent)
    }
}