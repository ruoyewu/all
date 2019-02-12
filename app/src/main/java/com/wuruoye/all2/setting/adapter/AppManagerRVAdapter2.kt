package com.wuruoye.all2.setting.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.wuruoye.all2.R
import com.wuruoye.all2.base.util.loadImage
import com.wuruoye.all2.setting.model.bean.ManageredApp
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

/**
 * Created by wuruoye on 2018/3/7.
 * this file is to
 */

class AppManagerRVAdapter2(
        private val appList: ArrayList<ManageredApp>
) : RecyclerView.Adapter<AppManagerRVAdapter2.ViewHolder>() {

    private var mOnActionListener: OnActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_app_manager_app_2, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = appList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = appList[position]
        with(holder) {
            civ.context.loadImage(app.icon, civ)
            tv.text = app.title
            switch.isChecked = app.isChecked
            switch.setOnCheckedChangeListener { _, isChecked ->
                app.isChecked = isChecked
                mOnActionListener?.onCheckChange(appList)
            }
        }
    }

    public fun setOnActionListener(onActionListener: OnActionListener) {
        mOnActionListener = onActionListener
    }

    public fun positionChange() {
        mOnActionListener?.onPositionChange(appList)
    }

    public fun getResult(): ArrayList<ManageredApp> = appList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val civ = itemView.findViewById<CircleImageView>(R.id.civ_app_manager_2)!!
        val tv = itemView.findViewById<TextView>(R.id.tv_app_manager_2)!!
        val switch = itemView.findViewById<Switch>(R.id.switch_app_manager_2)!!
    }

    public class TouchHelperCallBack(
            val adapter: AppManagerRVAdapter2
    ) : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView?,
                                      viewHolder: RecyclerView.ViewHolder?): Int =
                makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                        0)

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

        }

        override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?,
                            target: RecyclerView.ViewHolder?): Boolean {
            val fromP = viewHolder!!.adapterPosition
            val toP = target!!.adapterPosition
            Collections.swap(adapter.appList, fromP, toP)
            adapter.notifyItemMoved(fromP, toP)
            adapter.positionChange()
            return true
        }

    }

    public interface OnActionListener {
        fun onCheckChange(appList: ArrayList<ManageredApp>)
        fun onPositionChange(appList: ArrayList<ManageredApp>)
    }
}