package com.wuruoye.all2.setting.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.wuruoye.all2.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by wuruoye on 2017/10/17.
 * this file is to do
 */
class AppMangerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tv = itemView.findViewById<TextView>(R.id.tv_app_manager_title)!!
    val civ = itemView.findViewById<CircleImageView>(R.id.civ_app_manager_icon)!!

    init {
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
//        layoutParams.setMargins(10, 10, 10, 10)
        itemView.layoutParams = layoutParams
    }
}