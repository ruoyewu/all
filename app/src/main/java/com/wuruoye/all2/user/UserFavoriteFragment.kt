package com.wuruoye.all2.user

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.wuruoye.all2.R
import com.wuruoye.all2.base.util.getIntent
import com.wuruoye.all2.base.util.toast
import com.wuruoye.all2.user.adapter.FavoriteRVAdapter
import com.wuruoye.all2.user.model.bean.ArticleFavorite
import com.wuruoye.all2.user.model.bean.ArticleFavoriteItem
import com.wuruoye.all2.user.presenter.UserGet
import com.wuruoye.all2.user.presenter.UserView
import com.wuruoye.all2.v3.adapter.viewholder.HeartRefreshViewHolder
import com.wuruoye.all2.v3.model.bean.ArticleListItem
import kotlinx.android.synthetic.main.fragment_user_favorite.*

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
class UserFavoriteFragment : RefreshFragment(){
    private var mUserId = 0
    private var isMore = true
    private var isFirst = true

    private lateinit var mUserGet: UserGet
    private val mUserView = object : UserView{
        override fun onAvatarUpload(result: Boolean) {

        }

        override fun onFavoriteGet(model: ArticleFavorite) {
            activity?.runOnUiThread {
                setRecyclerView(model)
            }
        }

        override fun setWorn(message: String) {
            activity?.runOnUiThread {
                toast(message)
            }
        }

        override fun onReadTimeUpload(result: Boolean) {

        }
    }

    private lateinit var refreshVH: HeartRefreshViewHolder
    private val onItemClickListener = object : FavoriteRVAdapter.OnItemClickListener{
        override fun onLoadMore(next: Long, vh: HeartRefreshViewHolder) {
            refreshVH = vh
            loadMore(next)
        }

        override fun onItemClick(item: ArticleFavoriteItem) {
            val key = item.key.split("_")
            val name = key[0]
            val category = key[1]
            val newItem = Gson().fromJson(item.info, ArticleListItem::class.java)
            val intent = context!!.getIntent(newItem, name, category)
            val activity = activity as UserActivity
            if (intent != null){
                activity.startThisActivity(intent)
            }
        }

    }

    override val contentView: Int
        get() = R.layout.fragment_user_favorite

    override fun initData(bundle: Bundle?) {
        mUserId = bundle!!.getInt("userid")
        mUserGet = UserGet(context!!)
        mUserGet.attachView(mUserView)
    }

    override fun initView(view: View) {
    }

    override fun onResume() {
        super.onResume()
        isFirst = true
        isMore = true
        loadMore(0L)
    }

    override fun refresh() {

    }

    private fun loadMore(next: Long){
        if (isMore){
            mUserGet.getFavorite(mUserId, next)
        }else {
            refreshVH.hv.visibility = View.GONE
            refreshVH.tv.visibility = View.VISIBLE
        }
    }

    private fun setRecyclerView(data: ArticleFavorite){
        if (isFirst){
            val adapter = FavoriteRVAdapter(context!!, data, onItemClickListener)
            rl_favorite.adapter = adapter
            rl_favorite.layoutManager = LinearLayoutManager(context)
            isFirst = false
        }else {
            if (data.info.size > 0) {
                val adapter = rl_favorite.adapter as FavoriteRVAdapter
                adapter.setNext(data.next)
                adapter.addItems(data.info)
            }
        }
        if (data.info.size < 10){
            isMore = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mUserGet.detachView()
    }
}