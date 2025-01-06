package com.petid.petid.ui.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.petid.data.source.local.entity.NotificationEntity
import com.petid.petid.R
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.databinding.ItemNotificationBinding

class NotificationListAdapter(
    private val onButtonClick: (Long, String) -> Unit
) : ListAdapter<NotificationEntity, NotificationListAdapter.ViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<NotificationEntity>() {
            override fun areContentsTheSame(oldItem: NotificationEntity,
                                            newItem: NotificationEntity
            ) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: NotificationEntity,
                                         newItem: NotificationEntity
            ) =
                oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        with(holder) {
            title.text = item.title

            binding.root.setOnClickListener {
                if (!item.isChecked)
                    onButtonClick(item.id, item.title)
            }

            if(item.isChecked) {
                listOf(type, title).forEach {
                    it.setTextColor(getGlobalContext().getColor(R.color.b4))
                }
            }
        }
    }

    inner class ViewHolder(val binding: ItemNotificationBinding)
        : RecyclerView.ViewHolder(binding.root) {
        val type = binding.textViewType
        val title = binding.textViewTitle
    }
}