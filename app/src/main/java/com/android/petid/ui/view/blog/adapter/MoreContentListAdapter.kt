package com.android.petid.ui.view.blog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.domain.entity.ContentEntity
import com.android.petid.R
import com.android.petid.databinding.ItemMoreContentBinding
import com.bumptech.glide.Glide

class MoreContentListAdapter(
    private val mContext: Context,
    private val onItemClick: (ContentEntity) -> Unit
): ListAdapter<ContentEntity, MoreContentListAdapter.Holder>(diffUtil)  {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ContentEntity>() {
            override fun areContentsTheSame(oldItem: ContentEntity,newItem: ContentEntity) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: ContentEntity, newItem: ContentEntity) =
                oldItem.contentId == newItem.contentId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreContentListAdapter.Holder {
        val binding = ItemMoreContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: MoreContentListAdapter.Holder, position: Int) {
        val item = currentList[position]
        with(holder) {
            title.text = item.title

            val emptyImg = R.drawable.img_hospital_list_empty
            item.imageUrl?.let {
                Glide.with(mContext)
                    .load(it)
                    .placeholder(emptyImg)
                    .error(emptyImg)
                    .into(thumbnail)
            }

            itemView.setOnClickListener{
                onItemClick(item)
            }
        }
    }

    inner class Holder(val binding: ItemMoreContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val thumbnail = binding.imageViewThumbnail
        val title = binding.textViewTitle
    }

}