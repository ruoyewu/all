package com.wuruoye.all2.user.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.Gson
import com.wuruoye.all2.R
import com.wuruoye.all2.base.util.loadImage
import com.wuruoye.all2.user.model.ArticleFavorite
import com.wuruoye.all2.user.model.ArticleFavoriteItem
import com.wuruoye.all2.v3.adapter.viewholder.HeartRefreshViewHolder
import com.wuruoye.all2.v3.model.AppInfoCache
import com.wuruoye.all2.v3.model.ArticleListItem

/**
 * Created by wuruoye on 2017/9/28.
 * this file is to do
 */
class FavoriteRVAdapter(
        private val context: Context,
        private val data: ArticleFavorite,
        private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val appInfoCache = AppInfoCache(context)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position >= data.info.size){
            val viewHolder = holder as HeartRefreshViewHolder
            listener.onLoadMore(data.next, viewHolder)
        }else{
            val item = data.info[position]
            val name = item.key.split("_")[0]
            val info = Gson().fromJson(item.info, ArticleListItem::class.java)
            val viewHolder = holder as FavoriteViewHolder
            viewHolder.itemView.setOnClickListener { listener.onItemClick(item) }
            with(viewHolder){
                context.loadImage(appInfoCache.getAppIcon(name), ivIcon)
                context.loadImage(info.image, ivImg)
                tvTitle.text = info.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder =
            when (viewType){
                TYPE_REFRESH -> {
                    HeartRefreshViewHolder(
                            LayoutInflater.from(context)
                                    .inflate(R.layout.item_heart_refresh, null, false)
                    )
                }
                else -> {
                    FavoriteViewHolder(
                            LayoutInflater.from(context)
                                    .inflate(R.layout.item_favorite, null, false)
                    )
                }
            }

    override fun getItemViewType(position: Int): Int =
            if (position >= data.info.size){
                TYPE_REFRESH
            }else{
                TYPE_FAVORITE
            }

    override fun getItemCount(): Int =
            data.info.size + 1

    fun setNext(next: Long){
        data.next = next
    }

    fun addItems(items: ArrayList<ArticleFavoriteItem>){
        data.info.addAll(items)
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onLoadMore(next: Long, vh: HeartRefreshViewHolder)
        fun onItemClick(item: ArticleFavoriteItem)
    }

    companion object {
        val TYPE_REFRESH = 1
        val TYPE_FAVORITE = 2
    }
}