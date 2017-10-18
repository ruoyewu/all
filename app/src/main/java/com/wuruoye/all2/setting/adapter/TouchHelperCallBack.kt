package com.wuruoye.all2.setting.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.wuruoye.all2.base.util.loge
import java.util.*

/**
 * Created by wuruoye on 2017/10/17.
 * this file is to do
 */
class TouchHelperCallBack(
        private val mAdapter: AppManagerRVAdapter
) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int =
            makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        val fromPosition = viewHolder!!.adapterPosition
        val toPosition = target!!.adapterPosition
        loge("on move : $fromPosition , $toPosition")
        if (toPosition == 0 || fromPosition == 0){

        }else{
            Collections.swap(mAdapter.appList, fromPosition, toPosition)
            mAdapter.notifyItemMoved(fromPosition, toPosition)
            mAdapter.saveData()
        }
//        if (fromPosition < toPosition){
//            for (i in fromPosition until toPosition){
//                Collections.swap(mAdapter.appList, i, i + 1)
//            }
//        }else{
//            for (i in fromPosition downTo toPosition + 1){
//                Collections.swap(mAdapter.appList, i, i - 1)
//            }
//        }
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

    }

    override fun isLongPressDragEnabled(): Boolean = true
}