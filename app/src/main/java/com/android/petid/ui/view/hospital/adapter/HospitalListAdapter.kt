package com.android.petid.ui.view.hospital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.domain.entity.HospitalEntity
import com.android.domain.entity.HospitalOrderDetailEntity
import com.android.petid.databinding.ItemHospitalBinding

class HospitalListAdapter(
    private val mContext: Context,
    private val onItemClick: (HospitalEntity) -> Unit
) : ListAdapter<HospitalEntity, HospitalListAdapter.Holder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<HospitalEntity>() {
            override fun areContentsTheSame(oldItem: HospitalEntity,newItem: HospitalEntity) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: HospitalEntity, newItem: HospitalEntity) =
                oldItem.id == newItem.id
        }
    }

    inner class Holder(val binding: ItemHospitalBinding) : RecyclerView.ViewHolder(binding.root) {
        val thumbnail = binding.imageViewThumbnail
        val name = binding.textViewName
        val address = binding.textViewAddress
        val vet = binding.textViewVet
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemHospitalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val hospitalItem = currentList[position]
        with(holder) {
            name.text = hospitalItem.name
            address.text = hospitalItem.address
            vet.text = hospitalItem.vet + " 원장"

            // 병원 이미지 로드 (Glide 사용)
            /*hospitalItem.imageUrl?.let {
                Glide.with(mContext)
                    .load(it)
                    .into(thumbnail)
            }*/

            itemView.setOnClickListener{
                onItemClick(hospitalItem)
            }
        }
    }
}