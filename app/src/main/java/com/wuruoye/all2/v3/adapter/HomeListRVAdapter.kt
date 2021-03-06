package com.wuruoye.all2.v3.adapter

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.wuruoye.all2.R
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.toast
import com.wuruoye.all2.v3.adapter.viewholder.HeartRefreshViewHolder
import com.wuruoye.all2.v3.model.bean.AppInfo
import com.wuruoye.all2.v3.model.bean.ArticleList
import com.wuruoye.all2.v3.model.bean.ArticleListItem
import com.wuruoye.all2.v3.presenter.AppListGet
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
class HomeListRVAdapter(
        private val infoList: HashMap<String, AppInfo>,
        private val appNameList: ArrayList<String>,
        private val onItemClickListener: OnItemClickListener,
        private val isNetRefresh: Boolean,
        private val rlMain: RecyclerView
) : RecyclerView.Adapter<HomeListRVAdapter.ViewHolder>() {

    private lateinit var context: Context

    private val rlMaps = HashMap<String, RecyclerView>()
    private val dataMaps = HashMap<String, ArticleList>()
    private val btnMaps = HashMap<String, Button>()
    private val isShowAll = HashMap<String, Boolean>()

    private lateinit var appListGet2: AppListGet
    private val mView = object : AbsView<ArticleList>{
        override fun setModel(model: ArticleList) {
            (context as Activity).runOnUiThread {
                log("data result")
                dataMaps.put(model.name, model)
                setRecyclerView(model)
            }
        }

        override fun setWorn(message: String) {
            (context as Activity).runOnUiThread {
                toast(message)
            }
        }

    }
    private val mListener = object : AllListRVAdapter.OnItemClickListener{
        override fun loadMore(next: String, vh: HeartRefreshViewHolder) {

        }

        override fun onItemClick(item: ArticleListItem, name: String, category: String) {
            onItemClickListener.onItemClick(item, name, category)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appInfo = infoList[appNameList[position]]!!
        with(holder){
            tvTitle.text = appInfo.title
            itemView.setOnLongClickListener { onItemClickListener.onLongClick(appInfo) }
            btnEnter.setOnClickListener { onItemClickListener.enterApp(appInfo) }
            btnMore.setOnClickListener { showAll(appInfo.name) }
            Glide.with(context)
                    .load(appInfo.icon)
                    .into(ivIcon)
            rlList.layoutManager = LinearLayoutManager(context)
            val itemAnimator = FadeInLeftAnimator(OvershootInterpolator(1f))
            itemAnimator.addDuration = ANIMATOR_DURATION
            itemAnimator.removeDuration = ANIMATOR_DURATION
            rlList.itemAnimator = itemAnimator
            rlMaps.put(appInfo.name, rlList)
            btnMaps.put(appInfo.name, btnMore)
            isShowAll.put(appInfo.name, false)
            if (dataMaps[appInfo.name] != null){
                log("get data now")
                setRecyclerView(dataMaps[appInfo.name]!!)
            }else{
                if (!isNetRefresh) {
                    log("get data local")
                    appListGet2.requestArticleList(appInfo.name, appInfo.category_name[0], "0", AbsPresenter.Method.LOCAL)
                }else{
                    log("get data net")
                    appListGet2.requestArticleList(appInfo.name, appInfo.category_name[0], "0", AbsPresenter.Method.NET)
                }
            }
        }
    }

    override fun getItemCount(): Int = appNameList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home_app, parent, false)
        context = parent.context
        appListGet2 = AppListGet(context)
        appListGet2.attachView(mView)

        return ViewHolder(view)
    }

    private fun showAll(name: String){
        if (rlMaps[name] != null && dataMaps[name] != null && isShowAll[name] != null){
            isShowAll[name] = !isShowAll[name]!!
            val adapter = rlMaps[name]!!.adapter as AllListRVAdapter
            if (isShowAll[name]!!){
                btnMaps[name]!!.text = "收起更多"
                for (i in 1 until dataMaps[name]!!.list.size){
                    val delay = (i - 1) * ANIMATOR_DELAY
                    Handler().postDelayed({
                        adapter.addItem(i, dataMaps[name]!!.list[i])
                    }, delay)
                }
            }else{
                btnMaps[name]!!.text = "显示更多"
                for (i in dataMaps[name]!!.list.size - 1 downTo 1){
                    adapter.removeItem(i)
                }
                Handler().postDelayed({
                    rlMain.smoothScrollToPosition(getPosition(name))
                }, 20)
            }
        }else{
            onItemClickListener.onError("请等待数据加载完成...")
        }
    }

    private fun setRecyclerView(model: ArticleList){
        val arrayList = ArrayList<ArticleListItem>()
        arrayList.add(model.list[0])
        btnMaps[model.name]!!.text = "显示更多"
        val newModel = ArticleList(model.name, model.category, model.next, arrayList)
        val rl = rlMaps[model.name]
        val adapter = AllListRVAdapter(false, newModel, mListener)
        rl!!.adapter = adapter
    }

    private fun getPosition(name: String): Int{
        return (0 until infoList.size)
                .firstOrNull { name == appNameList[it] }
                ?: infoList.size - 1
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
        fun enterApp(item: AppInfo)
        fun onItemClick(item: ArticleListItem, name: String, category: String)
        fun onError(message: String)
        fun onLongClick(item: AppInfo): Boolean
    }

    companion object {
        const val ANIMATOR_DURATION = 200L
        const val ANIMATOR_DELAY = 50L
    }

    private fun log(message: String){
//        loge("HomeListRVAdapter: $message")
    }
}