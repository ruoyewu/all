package com.wuruoye.all2.v3.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.wuruoye.all2.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by wuruoye on 2017/9/21.
 * this file is to do
 */
class ArticleCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivUser = itemView.findViewById<CircleImageView>(R.id.iv_comment_user)!!
    val tvUser = itemView.findViewById<TextView>(R.id.tv_comment_user)!!
    val tvTime = itemView.findViewById<TextView>(R.id.tv_comment_time)!!
    val tvParent = itemView.findViewById<TextView>(R.id.tv_comment_parent)!!
    val tvContent = itemView.findViewById<TextView>(R.id.tv_comment_content)!!
    val llLove = itemView.findViewById<LinearLayout>(R.id.ll_comment_love)!!
    val ivLove = itemView.findViewById<ImageView>(R.id.iv_comment_love)!!
    val tvLove = itemView.findViewById<TextView>(R.id.tv_comment_love)!!

    init {
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
//        layoutParams.setMargins(10, 10, 10, 10)
        itemView.layoutParams = layoutParams
    }
}