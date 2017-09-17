package com.wuruoye.all2.v3.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wuruoye.all2.R
import kotlinx.android.synthetic.main.item_all_list.view.*

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */

class AllListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val tvTitle = itemView.findViewById<TextView>(R.id.tv_all_title)!!
    val tvAuthor = itemView.findViewById<TextView>(R.id.tv_all_author)!!
    val tvType = itemView.findViewById<TextView>(R.id.tv_all_type)!!
    val tvForward = itemView.findViewById<TextView>(R.id.tv_all_forward)!!
    val ivImg = itemView.findViewById<ImageView>(R.id.iv_all_img)!!
    val tvDate = itemView.findViewById<TextView>(R.id.tv_all_date)!!

    init {
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
//        layoutParams.setMargins(10, 10, 10, 10)
        itemView.layoutParams = layoutParams
    }
}