package com.wuruoye.all2.setting.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wuruoye.all2.R
import com.wuruoye.all2.base.util.loadImage
import com.wuruoye.all2.base.util.loge
import com.wuruoye.all2.v3.model.AppInfoCache
import java.lang.StringBuilder

/**
 * Created by wuruoye on 2017/10/17.
 * this file is to do
 */
class AppManagerRVAdapter(
        val appList: ArrayList<Array<String>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mContext: Context
    private lateinit var mAppInfoCache: AppInfoCache
    private var onLongPressListener: OnLongPressListener? = null

    override fun getItemCount(): Int =
            appList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)){
            TYPE_AV -> {
                val viewHolder = holder as AppManagerAVViewHolder
                if (position == 0){
                    viewHolder.tv.text = "首页展示栏目"
                }else{
                    viewHolder.tv.text = "首页不展示栏目"
                }
            }
            TYPE_NORMAL -> {
                val viewHolder = holder as AppMangerViewHolder
                viewHolder.itemView.setOnLongClickListener {
                    onLongPressListener?.onLongPress(viewHolder)
                    true
                }
                val array = appList[position]
                mContext.loadImage(array[2], viewHolder.civ)
                viewHolder.tv.text = array[1]
            }
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent!!.context
        mAppInfoCache = AppInfoCache(mContext)

        when (viewType){
            TYPE_AV -> {
                return AppManagerAVViewHolder(
                        LayoutInflater.from(mContext)
                                .inflate(R.layout.item_app_manager_text, null, false)
                )
            }
            TYPE_NORMAL -> {
                return AppMangerViewHolder(
                        LayoutInflater.from(mContext)
                                .inflate(R.layout.item_app_manager_app, null, false)
                )
            }
            else-> {
                throw IllegalArgumentException("viewType must be TYPE_AV or TYPE_NORMAL")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val array = appList[position]
        return if (array[0] == "av"){
            TYPE_AV
        }else{
            TYPE_NORMAL
        }
    }

    fun saveData(){
        val showList = ArrayList<String>()
        (1 until appList.size)
                .takeWhile { appList[it][0] != "av" }
                .mapTo(showList) { appList[it][0] }
        mAppInfoCache.putAvAppList(showList)
    }

    fun addOnLongPressListener(listener: OnLongPressListener){
        this.onLongPressListener = listener
    }

    interface OnLongPressListener{
        fun onLongPress(viewHolder: RecyclerView.ViewHolder)
    }

    companion object {
        val TYPE_AV = 1
        val TYPE_NORMAL = 2
    }
}