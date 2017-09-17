package com.wuruoye.all2.v3.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.ArrayMap
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.transitionseverywhere.*
import com.wuruoye.all2.R
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.v3.model.AppInfo
import com.wuruoye.all2.v3.model.AppList
import de.hdodenhof.circleimageview.CircleImageView

import com.wuruoye.all2.base.util.extensions.toast
import com.wuruoye.all2.v3.model.ListItem
import com.wuruoye.all2.v3.presenter.AppListGet

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
class HomeListRVAdapter(
        private val infoList: ArrayList<AppInfo>,
        private val onItemClickListener: OnItemClickListener,
        private val isNetRefresh: Boolean
) : RecyclerView.Adapter<HomeListRVAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var isShowAll = false

    private val rlMaps = HashMap<String, RecyclerView>()
    private val dataMaps = HashMap<String, AppList>()
    private val btnMaps = HashMap<String, Button>()


    private lateinit var appListGet: AppListGet
    private val mView = object : AbsView<AppList>{
        override fun setModel(model: AppList) {
            (context as Activity).runOnUiThread {
                dataMaps.put(model.name, model)
                setRecyclerView(model)
            }
        }

        override fun setWorn(message: String) {
            (context as Activity).runOnUiThread {
                context.toast(message)
            }
        }

    }
    private val mListener = object : AllListRVAdapter.OnItemClickListener{
        override fun loadMore(next: String, tv: TextView) {

        }

        override fun onItemClick(item: ListItem, name: String, category: String) {
            onItemClickListener.onItemClick(item, name, category)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val appInfo = infoList[position]
        with(holder!!){
            tvTitle.text = appInfo.title
            btnEnter.setOnClickListener { onItemClickListener.enterApp(position) }
            btnMore.setOnClickListener { showAll(appInfo.name) }
            Glide.with(context)
                    .load(appInfo.icon)
                    .into(ivIcon)
            rlList.layoutManager = LinearLayoutManager(context)
            rlMaps.put(appInfo.name, rlList)
            btnMaps.put(appInfo.name, btnMore)
            if (!isNetRefresh) {
                appListGet.requestData(appInfo.name, appInfo.category_name[0], "0", AbsPresenter.Method.LOCAL)
            }else{
                appListGet.requestData(appInfo.name, appInfo.category_name[0], "0", AbsPresenter.Method.NET)
            }
        }
    }

    override fun getItemCount(): Int = infoList.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_home_app, parent, false)
        context = parent.context
        appListGet = AppListGet(context)
        appListGet.attachView(mView)
        return ViewHolder(view)
    }

    private fun showAll(name: String){
        if (rlMaps[name] != null && dataMaps[name] != null){
            val set = TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(Fade())
            isShowAll = !isShowAll
            val newModel =
                    if (isShowAll){
                        set.addTransition(Slide(Gravity.BOTTOM))
                        btnMaps[name]!!.text = "收起更多"
                        dataMaps[name]!!
                    }else{
                        set.addTransition(Slide(Gravity.TOP))
                        btnMaps[name]!!.text = "显示更多"
                        val model = dataMaps[name]!!
                        val arrayList = ArrayList<ListItem>()
                        arrayList.add(model.list[0])
                        AppList(model.name, model.category, model.next, arrayList)
                    }
            TransitionManager.beginDelayedTransition(rlMaps[name]!!, set)
            val adapter = rlMaps[name]!!.adapter as AllListRVAdapter
            adapter.changeData(newModel)
        }else{
            onItemClickListener.onError("请等待数据加载完成...")
        }
    }

    private fun setRecyclerView(model: AppList){
        val arrayList = ArrayList<ListItem>()
        arrayList.add(model.list[0])
        val newModel = AppList(model.name, model.category, model.next, arrayList)
        val rl = rlMaps[model.name]
        val adapter = AllListRVAdapter(false, newModel, mListener)
        rl!!.adapter = adapter
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon = view.findViewById<CircleImageView>(R.id.civ_home_icon)!!
        val tvTitle = view.findViewById<TextView>(R.id.tv_home_title)!!
        val rlList = view.findViewById<RecyclerView>(R.id.rl_home_list)!!
        val btnMore = view.findViewById<Button>(R.id.btn_home_more)!!
        val btnEnter = view.findViewById<Button>(R.id.btn_home_enter)!!

        init {
            val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(20, 20, 20, 20)
            itemView.layoutParams = layoutParams
        }
    }

    interface OnItemClickListener{
        fun showMore(position: Int)
        fun enterApp(position: Int)
        fun onItemClick(item: ListItem, name: String, category: String)
        fun onError(message: String)
    }
}