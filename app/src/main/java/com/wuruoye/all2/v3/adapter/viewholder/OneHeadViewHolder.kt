package com.wuruoye.all2.v3.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wuruoye.all2.R
import kotlinx.android.synthetic.main.item_one_head.*

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
class OneHeadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvDate = itemView.findViewById<TextView>(R.id.tv_one_date)!!
    val tvImgAuthor = itemView.findViewById<TextView>(R.id.tv_one_image_author)!!
    val tvForward = itemView.findViewById<TextView>(R.id.tv_one_content)!!
    val tvForwardAuthor = itemView.findViewById<TextView>(R.id.tv_one_content_author)!!
    val ivImg = itemView.findViewById<ImageView>(R.id.iv_one_image)!!

    init {
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
//        layoutParams.setMargins(10, 10, 10, 10)
        itemView.layoutParams = layoutParams
    }
}