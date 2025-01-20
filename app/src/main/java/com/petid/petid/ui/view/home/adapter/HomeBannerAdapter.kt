package com.petid.petid.ui.view.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.petid.domain.entity.BannerEntity
import com.petid.petid.R

class HomeBannerAdapter(
    private val mContext: Context,
    private val onItemClick: (Int) -> Unit
): ListAdapter<BannerEntity, HomeBannerAdapter.CustomViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<BannerEntity>() {
            override fun areContentsTheSame(oldItem: BannerEntity, newItem: BannerEntity) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: BannerEntity, newItem: BannerEntity) =
                oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_banner, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPosition = this.layoutPosition
                currentList[curPosition % currentList.size].contentId?.let { it1 -> onItemClick(it1) }
            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        if (currentList.size != 0) {
            val currentItem = currentList[position % currentList.size]
            val defaultImg =
                AppCompatResources.getDrawable(mContext, R.drawable.layout_home_banner_background)

            with(holder) {
                Glide
                    .with(mContext)
                    .load(currentItem.imageUrl)
                    .placeholder(defaultImg)
                    .error(defaultImg)
                    .into(imageView)

                title.text = currentItem.text
            }
        }
    }

    fun getListSize() : Int {
        return if(currentList.size == 0) 1 else currentList.size
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE // 뷰페이저 전환이 무한"처럼" 보이도록 굉장히 큰 억단위 수를 아이템 수로 임의로 넣어줌
    }


    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView_banner_image)
        val title: TextView = itemView.findViewById(R.id.textView_title)
    }

}