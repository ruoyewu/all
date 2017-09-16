package com.wuruoye.all2.v3.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wuruoye.all2.R
import com.wuruoye.all2.v3.adapter.viewholder.AllListViewHolder
import com.wuruoye.all2.v3.adapter.viewholder.OneHeadViewHolder
import com.wuruoye.all2.v3.adapter.viewholder.RefreshViewHolder
import com.wuruoye.all2.v3.model.AppList
import com.wuruoye.all2.v3.model.ListItem
import com.wuruoye.all2.base.util.extensions.loadImage
import java.util.*

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
class AllListRVAdapter(
        private val isShowMore: Boolean,
        private var data: AppList,
        private val onItemClickListener: AllListRVAdapter.OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position >= data.list.size && isShowMore){
            val viewHolder: RefreshViewHolder = holder as RefreshViewHolder
            onItemClickListener.loadMore(data.next, viewHolder.tv)
        }else{
            val item = data.list[position]
            holder!!.itemView.setOnClickListener { onItemClickListener.onItemClick(item) }
            if (item.category_id == "0"){
                val viewHolder: OneHeadViewHolder = holder as OneHeadViewHolder
                val date = item.date.substring(0, 10).split("-")
                val info = item.other_info.split("|")
                with(viewHolder){
                    tvDate.text = date[0] + " / " + date[1] + " / " + date[2]
                    tvForward.text = item.forward
                    tvForwardAuthor.text = info[1]
                    tvImgAuthor.text = item.title + " | " + info[0]
                    itemView.context.loadImage(item.image, ivImg)
                }
            }else{
                val viewHolder: AllListViewHolder = holder as AllListViewHolder
                with(viewHolder){
                    if (item.type == ""){
                        tvType.visibility = View.GONE
                    }else{
                        tvType.visibility = View.VISIBLE
                        tvType.text = item.type
                    }
                    if (item.title == ""){
                        tvTitle.visibility = View.GONE
                    }else{
                        tvTitle.visibility = View.VISIBLE
                        tvTitle.text = item.title
                    }
                    if (item.author == ""){
                        tvAuthor.visibility = View.GONE
                    }else{
                        tvAuthor.visibility = View.VISIBLE
                        tvAuthor.text = "-\t" + item.author
                    }
                    if (item.image == ""){
                        ivImg.visibility = View.GONE
                    }else{
                        ivImg.visibility = View.VISIBLE
                        itemView.context.loadImage(item.image, ivImg)
                    }
                    if (item.forward == ""){
                        tvForward.visibility = View.GONE
                    }else{
                        tvForward.visibility = View.VISIBLE
                        tvForward.text = item.forward
                    }
                    if (item.age == ""){
                        if (item.date == ""){
                            if (item.time_millis == ""){
                                tvDate.visibility = View.GONE
                            }else{
                                tvDate.visibility = View.VISIBLE
                                val calendar = Calendar.getInstance()
                                calendar.timeInMillis = item.time_millis.toLong()
                                val date = calendar.get(Calendar.YEAR).toString() + "-" + (calendar.get(Calendar.MONTH) + 1) +
                                        "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR) + ":" +
                                        calendar.get(Calendar.MINUTE)
                                tvDate.text = date
                            }
                        }else{
                            tvDate.visibility = View.VISIBLE
                            tvDate.text = item.date.substring(0, 16)
                        }
                    }else{
                        tvDate.visibility = View.VISIBLE
                        tvDate.text = item.age
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder =
            when (viewType){
                TYPE_NORMAL -> {
                    AllListViewHolder(
                            LayoutInflater.from(parent!!.context)
                                    .inflate(R.layout.item_all_list, null, false)
                    )
                }
                TYPE_ONE_HEAD -> {
                    OneHeadViewHolder(
                            LayoutInflater.from(parent!!.context)
                                    .inflate(R.layout.item_one_head, null, false)
                    )
                }
                TYPE_REFRESH -> {
                    RefreshViewHolder(
                            LayoutInflater.from(parent!!.context)
                                    .inflate(R.layout.item_all_refresh, null, false)
                    )
                }
                else -> {
                    AllListViewHolder(
                            LayoutInflater.from(parent!!.context)
                                    .inflate(R.layout.item_all_list, null, false)
                    )
                }
            }

    override fun getItemViewType(position: Int): Int =
            if (position >= data.list.size){
                TYPE_REFRESH
            }else{
                val item = data.list[position]
                if (item.category_id == "0"){
                    TYPE_ONE_HEAD
                }else{
                    TYPE_NORMAL
                }
            }

    override fun getItemCount(): Int =
            if (isShowMore){
                data.list.size + 1
            }else{
                data.list.size
            }

    fun changeData(data: AppList){
        this.data = data
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun loadMore(next: String, tv: TextView)
        fun onItemClick(item: ListItem)
    }

    companion object {
        val TYPE_NORMAL = 1
        val TYPE_REFRESH = 2
        val TYPE_ONE_HEAD = 3
    }
}