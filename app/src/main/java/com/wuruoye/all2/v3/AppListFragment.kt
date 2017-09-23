package com.wuruoye.all2.v3

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.animation.OvershootInterpolator
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseFragment
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.extensions.loge
import com.wuruoye.all2.base.util.extensions.toast
import com.wuruoye.all2.v3.adapter.AllListRVAdapter
import com.wuruoye.all2.v3.adapter.HomeListRVAdapter
import com.wuruoye.all2.v3.adapter.viewholder.HeartRefreshViewHolder
import com.wuruoye.all2.v3.model.ArticleList
import com.wuruoye.all2.v3.model.ArticleListItem
import com.wuruoye.all2.v3.presenter.AppListGet
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * Created by wuruoye on 2017/9/17.
 * this file is to do
 */
class AppListFragment : BaseFragment() {
    private lateinit var mName: String
    private lateinit var mCategory: String
    private lateinit var mNext: String

    private var isLoadMore = false
    private lateinit var refreshVH: HeartRefreshViewHolder

    private lateinit var appListGet: AppListGet
    private val mView = object : AbsView<ArticleList>{
        override fun setModel(model: ArticleList) {
            activity.runOnUiThread{
                if (!isLoadMore) {
                    setRecyclerView(model)
                }else{
                    isLoadMore = false
                    setMore(model)
                }
            }
        }
        override fun setWorn(message: String) {
            activity.runOnUiThread{
                activity.toast(message)
            }
        }
    }
    private val onItemClickListener = object : AllListRVAdapter.OnItemClickListener{
        override fun loadMore(next: String, vh: HeartRefreshViewHolder) {
            this@AppListFragment.refreshVH = vh
            isLoadMore = true
            requestData(METHOD_NET)
        }

        override fun onItemClick(item: ArticleListItem, name: String, category: String) {
            when (item.open_type){
                MainActivity.TYPE_ARTICLE -> {
                    if (item.category_id != "0") {
                        val bundle = Bundle()
                        bundle.putParcelable("item", item)
                        bundle.putString("name", name)
                        if (item.category_id != ""){
                            bundle.putString("category", item.category_id)
                        }else {
                            bundle.putString("category", category)
                        }
                        val intent = Intent(context, ArticleDetailActivity::class.java)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                }
                MainActivity.TYPE_URL -> {

                }
                MainActivity.TYPE_IMG -> {

                }
                MainActivity.TYPE_VIDEO -> {

                }
            }
        }

    }

    override val contentView: Int
        get() = R.layout.fragment_list

    override fun initData(bundle: Bundle?) {
        mName = bundle!!.getString("name")
        mCategory = bundle.getString("category")
        mNext = "0"

        appListGet = AppListGet(context)
        appListGet.attachView(mView)
    }

    override fun initView(view: View) {
        srl_fragment_list.setOnRefreshListener { refreshData() }

        requestData(METHOD_LOCAL)
    }

    private fun refreshData(){
        mNext = "0"
        requestData(METHOD_NET)
    }

    private fun requestData(method: Int){
        if (method == METHOD_LOCAL){
            appListGet.requestArticleList(mName, mCategory, mNext, AbsPresenter.Method.LOCAL)
        }else{
            appListGet.requestArticleList(mName, mCategory, mNext, AbsPresenter.Method.NET)
        }
    }

    private fun setRecyclerView(data: ArticleList){
        srl_fragment_list.isRefreshing = false
        mNext = data.next
        val adapter = AllListRVAdapter(true, data, onItemClickListener)
        rl_fragment_list.layoutManager = LinearLayoutManager(context)
        val itemAnimator = FadeInLeftAnimator(OvershootInterpolator(1f))
        itemAnimator.addDuration = HomeListRVAdapter.ANIMATOR_DURATION
        itemAnimator.removeDuration = HomeListRVAdapter.ANIMATOR_DURATION
        rl_fragment_list.itemAnimator = itemAnimator
        rl_fragment_list.adapter = adapter
    }

    private fun setMore(data: ArticleList){
        if (data.list.size > 0){
            mNext = data.next
            try {
                val adapter = rl_fragment_list.adapter as AllListRVAdapter
                adapter.addItems(data.list)
            } catch (e: Exception) {
                loge("setMore: $mName : $mCategory : $mNext")
            }
        }else{
            refreshVH.hv.visibility = View.GONE
            refreshVH.tv.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        appListGet.detachView()
    }

    override fun onDestroy() {
        super.onDestroy()
        appListGet.detachView()
    }

    companion object {
        val METHOD_LOCAL = 1
        val METHOD_NET = 2
    }
}