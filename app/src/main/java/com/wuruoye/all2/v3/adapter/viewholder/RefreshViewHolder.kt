package com.wuruoye.all2.v3.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.wuruoye.all2.R
import kotlinx.android.synthetic.main.item_all_refresh.*

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
class RefreshViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tv = itemView.findViewById<TextView>(R.id.tv_item_refresh)

    init {
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(10, 10, 10, 10)
        itemView.layoutParams = layoutParams
    }
}