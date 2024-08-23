package com.android.petid.view.hospital

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.petid.databinding.ItemHospitalBinding
import com.android.petid.view.home.BannerItem
import com.android.petid.view.sign.TermsActivity
import com.bumptech.glide.Glide

class HospitalListAdapter(
    private val hospitalList: ArrayList<HospitalItem>,
    private val mContext: Context,
    private val onItemClick: (HospitalItem) -> Unit
) : RecyclerView.Adapter<HospitalListAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemHospitalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val hospitalItem = hospitalList[position]
        with(holder) {
            name.text = hospitalItem.name
            address.text = hospitalItem.address
            vet.text = hospitalItem.vet + " 원장"

            // 병원 이미지 로드 (Glide 사용)
            hospitalItem.imageUrl?.let {
                Glide.with(mContext)
                    .load(it)
                    .into(thumbnail)
            }

            itemView.setOnClickListener{
                onItemClick(hospitalItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return hospitalList.size
    }

    inner class Holder(val binding: ItemHospitalBinding) : RecyclerView.ViewHolder(binding.root) {
        val thumbnail = binding.imageViewThumbnail
        val name = binding.textViewName
        val address = binding.textViewAddress
        val vet = binding.textViewVet

    }
}