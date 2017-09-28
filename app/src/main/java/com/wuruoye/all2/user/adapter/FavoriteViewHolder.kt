package com.wuruoye.all2.user.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wuruoye.all2.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_favorite.*

/**
 * Created by wuruoye on 2017/9/28.
 * this file is to do
 */
class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivIcon = itemView.findViewById<CircleImageView>(R.id.civ_favorite_app)!!
    val tvTitle = itemView.findViewById<TextView>(R.id.tv_favorite_title)!!
    val ivImg = itemView.findViewById<ImageView>(R.id.iv_favorite_img)!!

    init {
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
//        layoutParams.setMargins(10, 10, 10, 10)
        itemView.layoutParams = layoutParams
    }
}