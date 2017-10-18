package com.wuruoye.all2.setting.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.wuruoye.all2.R

/**
 * Created by wuruoye on 2017/10/17.
 * this file is to do
 */
class AppManagerAVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tv = itemView.findViewById<TextView>(R.id.tv_app_manager_av)!!

    init {
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
//        layoutParams.setMargins(10, 10, 10, 10)
        itemView.layoutParams = layoutParams
    }
}