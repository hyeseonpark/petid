package com.android.petid.ui.view.my.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.domain.entity.HospitalOrderDetailEntity
import com.android.petid.databinding.ItemHospitalBinding
import com.android.petid.databinding.ItemHospitalReservationHistoryBinding

class HospitalReservationListAdapter(
    private val hospitalOrderDetailList: List<HospitalOrderDetailEntity>,
    private val mContext: Context
) : RecyclerView.Adapter<HospitalReservationListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemHospitalReservationHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val hospitalOrderDetailItem = hospitalOrderDetailList[position]
        with(holder) {
            // dateTime.text = hospitalOrderDetailItem.hospitalId
        }
    }

    override fun getItemCount(): Int {
        return hospitalOrderDetailList.size
    }

    inner class Holder(val binding: ItemHospitalReservationHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val hospitalName = binding.textViewHospitalName
        val dateTime = binding.textViewDateTime
    }
}