package com.wuruoye.all2.v3.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wuruoye.all2.R
import com.wuruoye.all2.base.util.DateUtil
import com.wuruoye.all2.base.util.loadUserImage
import com.wuruoye.all2.v3.adapter.viewholder.ArticleCommentViewHolder
import com.wuruoye.all2.v3.adapter.viewholder.HeartRefreshViewHolder
import com.wuruoye.all2.v3.model.bean.ArticleComment
import com.wuruoye.all2.v3.model.bean.ArticleCommentItem

/**
 * Created by wuruoye on 2017/9/21.
 * this file is to do
 */
class ArticleCommentRVAdapter(
        private val comment: ArticleComment,
        private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position >= comment.list.size){
            val viewHolder = holder as HeartRefreshViewHolder
            onItemClickListener.onLoadMore(comment.next, viewHolder)
        }else{
            val item = comment.list[position]
            val viewHolder = holder as ArticleCommentViewHolder
            with(viewHolder){
                itemView.setOnClickListener { onItemClickListener.onItemClick(item) }
                ivUser.setOnClickListener { onItemClickListener.onUserClick(item) }

                itemView.context.loadUserImage(item.username, ivUser)
                tvUser.text = item.username
                tvTime.text = DateUtil.getDateString(item.time)
                tvContent.text = item.content
                if (item.parent == ""){
                    tvParent.visibility = View.GONE
                }else{
                    tvParent.visibility = View.VISIBLE
                    tvParent.text = item.parent
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder =
            when (viewType){
                TYPE_REFRESH -> {
                    HeartRefreshViewHolder(
                            LayoutInflater.from(parent!!.context)
                                    .inflate(R.layout.item_heart_refresh, null, false)
                    )
                }
                else -> {
                    ArticleCommentViewHolder(
                            LayoutInflater.from(parent!!.context)
                                    .inflate(R.layout.item_comment_list, null, false)
                    )
                }
            }

    override fun getItemCount(): Int =
            comment.list.size + 1


    override fun getItemViewType(position: Int): Int =
            if (position >= comment.list.size){
                TYPE_REFRESH
            }else{
                TYPE_NORMAL
            }

    fun addItem(item: ArticleCommentItem){
        comment.list.add(0, item)
        notifyItemInserted(0)
    }

    fun addItems(list: ArrayList<ArticleCommentItem>){
        comment.list.addAll(list)
        notifyDataSetChanged()
    }

    fun setNext(next: Long){
        comment.next = next
    }

    interface OnItemClickListener{
        fun onLoadMore(next: Long, hv: HeartRefreshViewHolder)
        fun onItemClick(item: ArticleCommentItem)
        fun onUserClick(item: ArticleCommentItem)
    }

    companion object {
        val TYPE_REFRESH = 1
        val TYPE_NORMAL = 2
    }
}