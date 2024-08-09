package com.android.petid.view.hospital

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.petid.databinding.ItemHospitalBinding

class HospitalListAdapter : RecyclerView.Adapter<HospitalListAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalListAdapter.Holder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: HospitalListAdapter.Holder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class Holder(val binding: ItemHospitalBinding) : RecyclerView.ViewHolder(binding.root) {
        val thumbnail = binding.imageViewThumbnail
        val name = binding.textViewName
        val address = binding.textViewAddress
        val vet = binding.textViewVet
    }
}