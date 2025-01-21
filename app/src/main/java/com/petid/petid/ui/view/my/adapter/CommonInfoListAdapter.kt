package com.petid.petid.ui.view.my.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.petid.domain.entity.CommonInfo
import com.petid.petid.databinding.ItemCommonInfoBinding
import com.petid.petid.util.setTextWithEllipsis

class CommonInfoListAdapter(
    private val mContext: Context,
    private val onButtonClick: (Int) -> Unit
) : ListAdapter<CommonInfo, CommonInfoListAdapter.ViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CommonInfo>() {
            override fun areContentsTheSame(oldItem: CommonInfo,
                                            newItem: CommonInfo
            ) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: CommonInfo,
                                         newItem: CommonInfo
            ) =
                oldItem.contentId == newItem.contentId
        }
    }

    inner class ViewHolder(val binding: ItemCommonInfoBinding)
        : RecyclerView.ViewHolder(binding.root) {
        val title = binding.textViewTitle
        val body = binding.textViewBody
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemCommonInfoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        with(holder) {
            title.text = item.title
            body.setTextWithEllipsis(
                Html.fromHtml(item.body, Html.FROM_HTML_MODE_LEGACY).toString())
            itemView.setOnClickListener{ onButtonClick(item.contentId) }
        }
    }

}