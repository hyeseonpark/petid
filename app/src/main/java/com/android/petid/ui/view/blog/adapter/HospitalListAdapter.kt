package com.android.petid.ui.view.blog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.domain.entity.ContentEntity
import com.android.domain.entity.HospitalEntity
import com.android.petid.databinding.ItemBlogContentBinding
import com.android.petid.databinding.ItemHospitalBinding
import com.android.petid.databinding.ItemHospitalByLocationBinding

class HospitalListAdapter(
    private val hospitalList: List<HospitalEntity>,
    private val mContext: Context,
    private val onItemClick: (HospitalEntity) -> Unit
): RecyclerView.Adapter<HospitalListAdapter.Holder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalListAdapter.Holder {
        val binding = ItemHospitalByLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: HospitalListAdapter.Holder, position: Int) {
        val hospitalItem = hospitalList[position]
        with(holder) {
            name.text = hospitalItem.name
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

    override fun getItemCount(): Int {
        return hospitalList.size
    }

    inner class Holder(val binding: ItemHospitalByLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        val thumbnail = binding.imageViewThumbnail
        val name = binding.textViewName
        val vet = binding.textViewVet
    }

}