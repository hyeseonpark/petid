package com.petid.petid.ui.view.hospital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.LocationEntity
import com.petid.petid.databinding.ItemLocationBinding

class LocationListAdapter(
    private val mContext: Context,
    private val onItemClick: (LocationEntity) -> Unit
) : ListAdapter<LocationEntity, LocationListAdapter.Holder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<LocationEntity>() {
            override fun areContentsTheSame(oldItem: LocationEntity, newItem: LocationEntity) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: LocationEntity, newItem: LocationEntity) =
                oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val locationItem = currentList[position]
        with(holder) {
            name.text = locationItem.name

            itemView.setOnClickListener{
                onItemClick(locationItem)
            }
        }
    }

    inner class Holder(val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.textViewTitle
    }

}