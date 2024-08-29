package com.android.petid.ui.view.hospital

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.petid.databinding.ItemLocationBinding

class LocationListAdapter(
private val locationList: ArrayList<LocationItem>,
private val mContext: Context,
private val onItemClick: (LocationItem) -> Unit
) : RecyclerView.Adapter<LocationListAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val locationItem = locationList[position]
        with(holder) {
            name.text = locationItem.location

            itemView.setOnClickListener{
                onItemClick(locationItem)
            }
        }
    }

    inner class Holder(val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.textViewTitle
    }

}