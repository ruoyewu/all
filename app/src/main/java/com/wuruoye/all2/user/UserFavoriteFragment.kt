package com.wuruoye.all2.user

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.wuruoye.all2.R
import com.wuruoye.all2.base.util.toast
import com.wuruoye.all2.user.adapter.FavoriteRVAdapter
import com.wuruoye.all2.user.model.ArticleFavorite
import com.wuruoye.all2.user.model.ArticleFavoriteItem
import com.wuruoye.all2.user.presenter.UserGet
import com.wuruoye.all2.user.presenter.UserView
import com.wuruoye.all2.v3.ArticleDetailActivity
import com.wuruoye.all2.v3.MainActivity
import com.wuruoye.all2.v3.adapter.viewholder.HeartRefreshViewHolder
import com.wuruoye.all2.v3.model.ArticleListItem
import kotlinx.android.synthetic.main.fragment_user_collect.*

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
class UserFavoriteFragment : RefreshFragment(){
    private lateinit var mUserName: String

    private lateinit var mUserGet: UserGet
    private val mUserView = object : UserView{
        override fun onFavoriteGet(model: ArticleFavorite) {
            activity.runOnUiThread {
                setRecyclerView(model)
            }
        }

        override fun setWorn(message: String) {
            activity.runOnUiThread {
                toast(message)
            }
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
            val item = Gson().fromJson(item.info, ArticleListItem::class.java)
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
        get() = R.layout.fragment_user_collect

    override fun initData(bundle: Bundle?) {
        mUserName = bundle!!.getString("username")

        mUserGet = UserGet()
        mUserGet.attachView(mUserView)
    }

    override fun initView(view: View) {
        initRecyclerView()
    }

    override fun refresh() {

    }

    private fun initRecyclerView(){
        val adapter = FavoriteRVAdapter(context, ArticleFavorite(), onItemClickListener)
        rl_favorite.layoutManager = LinearLayoutManager(context)
        rl_favorite.adapter = adapter
    }

    private fun loadMore(next: Long){
        mUserGet.getFavorite(mUserName, next)
    }

    private fun setRecyclerView(data: ArticleFavorite){
        if (data.info.size > 0) {
            val adapter = rl_favorite.adapter as FavoriteRVAdapter
            adapter.setNext(data.next)
            adapter.addItems(data.info)
        }else{
            refreshVH.hv.visibility = View.GONE
            refreshVH.tv.visibility = View.VISIBLE
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mUserGet.detachView()
    }
}