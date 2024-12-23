package com.petid.petid.ui.view.hospital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.petid.domain.entity.HospitalEntity
import com.petid.petid.R
import com.petid.petid.databinding.ItemHospitalBinding
import com.bumptech.glide.Glide

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
            vet.text = String.format(mContext.getString(R.string.to_vet), hospitalItem.vet)

            // 이미지
            (R.drawable.img_hospital_list_empty).let {
                val imgSource: Any? = when(hospitalItem.imageUrl[0]) {
                    "" -> AppCompatResources.getDrawable(mContext, it)
                    else -> hospitalItem.imageUrl[0]
                }

                Glide.with(mContext)
                    .load(imgSource)
                    .placeholder(it)
                    .error(it)
                    .into(thumbnail)
            }


            itemView.setOnClickListener{
                onItemClick(hospitalItem)
            }
        }
    }
}