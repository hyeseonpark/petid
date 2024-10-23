package com.android.petid.ui.view.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.domain.entity.BannerEntity
import com.android.petid.R
import com.bumptech.glide.Glide

class HomeBannerAdapter(
    private val imageList: List<BannerEntity>,
    private val mContext: Context
): RecyclerView.Adapter<HomeBannerAdapter.CustomViewHolder>() {

    private val total: Int = imageList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_banner, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPosition = this.position
                //val img: ModelImageOnly = imageList[curPosition]
                // 클릭 시 관련 페이지로 이동하기
                Toast.makeText(mContext, "${curPosition % total}번째 배너 클릭됨", Toast.LENGTH_SHORT).show()
//                val intent = Intent(mContext, DetailActivity::class.java)
//                mContext.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        // 아이템 수로 굉장히 큰 수를 줬으므로 position으로 어떤 수가 나오든 5로 나눈 나머지 값 순서의의 데이터를 사해 5단위로 데이터가 반복되도록 한다.
        // 다른 곳에서도 position값은 5로 나눈 나머지를 사용하면 된다.
        // Glide.with(mContext).load(imageList[position % total].imageUrl).into(holder.img)
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE // 뷰페이저 전환이 무한"처럼" 보이도록 굉장히 큰 억단위 수를 아이템 수로 임의로 넣어줌
    }


    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imageView_banner_image)
    }

}