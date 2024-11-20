package com.android.petid.ui.view.my.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.domain.entity.HospitalOrderDetailEntity
import com.android.petid.R
import com.android.petid.databinding.ItemHospitalReservationHistoryBinding
import com.android.petid.enum.ReservationStatus
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

class HospitalReservationListAdapter(
    private val mContext: Context,
    private val onButtonClick: (Int, String) -> Unit
) : ListAdapter<HospitalOrderDetailEntity, HospitalReservationListAdapter.ViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<HospitalOrderDetailEntity>() {
            override fun areContentsTheSame(oldItem: HospitalOrderDetailEntity,
                                            newItem: HospitalOrderDetailEntity
            ) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: HospitalOrderDetailEntity,
                                         newItem: HospitalOrderDetailEntity
            ) =
                oldItem.id == newItem.id
        }
    }

    inner class ViewHolder(val binding: ItemHospitalReservationHistoryBinding)
        : RecyclerView.ViewHolder(binding.root) {
            val statusChip = binding.imageStatusChip
            val hospitalName = binding.textViewHospitalName
            val dateTime = binding.textViewDateTime
            val button = binding.button
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemHospitalReservationHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // holder.bindHospitalReservationInfo(currentList[position])

        val item = currentList[position]
        with(holder) {

            hospitalName.text = item.hospitalName
            dateTime.text = formatInstantToDateTime(item.date)

            button.setOnClickListener {
                onButtonClick(item.id, item.status)
            }

            when(ReservationStatus.toValue(item.status)) {
                ReservationStatus.CONFIRMED -> {
                    statusChip.setImageResource(R.drawable.ic_chip_confirmed)

                    button.text = mContext.getString(R.string.cancel_reservation)
                    button.setBackgroundResource(R.drawable.button_background_f2)
                    button.setTextColor(mContext.getColor(R.color.petid_underbar))
                }
                ReservationStatus.PENDING -> {
                    statusChip.setImageResource(R.drawable.ic_chip_pending)

                    button.text = mContext.getString(R.string.cancel_reservation)
                    button.setBackgroundResource(R.drawable.button_background_f2)
                    button.setTextColor(mContext.getColor(R.color.petid_underbar))
                }
                ReservationStatus.COMPLETED -> {
                    statusChip.setImageResource(R.drawable.ic_chip_completed)

                    button.text = mContext.getString(R.string.write_review)
                    button.setBackgroundResource(R.drawable.button_background_common)
                    button.setTextColor(mContext.getColor(R.color.white))

                }
                ReservationStatus.CANCELLED -> {
                    statusChip.setImageResource(R.drawable.ic_chip_cancelled)

                    button.text = mContext.getString(R.string.re_reservation)
                    button.setBackgroundResource(R.drawable.button_background_common)
                    button.setTextColor(mContext.getColor(R.color.white))
                }
                else -> {}
            }
        }
    }

    /**
     * date 값 변환
     */
    private fun formatInstantToDateTime(timestamp: Long): String {
        val instant = Instant.ofEpochSecond(timestamp)

        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("M월 d일(E) HH:mm", Locale.KOREAN)

        return dateTime.format(formatter)
    }
}