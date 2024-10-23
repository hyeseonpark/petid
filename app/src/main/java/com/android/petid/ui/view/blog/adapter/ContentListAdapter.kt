package com.android.petid.ui.view.blog.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.domain.entity.ContentEntity
import com.android.domain.entity.HospitalEntity
import com.android.petid.R
import com.android.petid.databinding.ItemBlogContentBinding
import com.android.petid.enum.ContentCategoryType
import com.bumptech.glide.Glide

class ContentListAdapter(
    private val contentList: List<ContentEntity>,
    private val mContext: Context,
    private val doLike: (Int) -> Unit,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ContentListAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentListAdapter.Holder {
        val binding = ItemBlogContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: ContentListAdapter.Holder, position: Int) {
        val contentItem = contentList[position]
        with(holder) {
            contentTypeTextView.text = when(contentItem.category) {
                ContentCategoryType.RECOMMENDED.name -> mContext.getString(R.string.tab_recommendation_title)
                ContentCategoryType.TIPS.name -> mContext.getString(R.string.tab_tips_title)
                ContentCategoryType.ABOUTPET.name -> mContext.getString(R.string.tab_about_pet_title)
                ContentCategoryType.VENUE.name -> mContext.getString(R.string.tab_venue_title)
                ContentCategoryType.SUPPORT.name -> mContext.getString(R.string.tab_support_title)
                else -> ""
            }

            title.text = contentItem.title
            body.text = contentItem.body

            likeButton.text = when(contentItem.likesCount) {
                0 -> mContext.getString(R.string.like)
                else -> contentItem.likesCount.toString()
            }
            likeButton.isSelected = contentItem.isLiked
            likeButton.setOnClickListener {
                doLike(contentItem.contentId)
                if (!likeButton.isSelected) {
                    likeButton.isSelected = true
                }
            }

            /*contentItem.imageUrl?.let {
                Glide.with(mContext)
                    .load(it)
                    .into(imageViewContentPreview)
            }*/

            itemView.setOnClickListener {
                onItemClick(contentItem.contentId)
            }
        }
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    inner class Holder(val binding: ItemBlogContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageViewContentPreview = binding.imageViewContentPreview
        val title = binding.textViewContentTitle
        val body = binding.textViewContentBody
        val likeButton = binding.buttonContentLike
        val contentTypeTextView = binding.textViewContentCategory
    }
}