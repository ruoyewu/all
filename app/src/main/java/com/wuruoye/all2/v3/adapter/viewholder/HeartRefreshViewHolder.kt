package com.wuruoye.all2.v3.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.wuruoye.all2.R
import com.wuruoye.all2.base.widget.HeartbeatView
import kotlinx.android.synthetic.main.item_heart_refresh.*

/**
 * Created by wuruoye on 2017/9/21.
 * this file is to do
 */
class HeartRefreshViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val hv = itemView.findViewById<HeartbeatView>(R.id.hv_refresh)!!
    val tv = itemView.findViewById<TextView>(R.id.tv_refresh)!!

    init {
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(10, 10, 10, 10)
        itemView.layoutParams = layoutParams
    }
}