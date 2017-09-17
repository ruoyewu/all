package com.wuruoye.all2.v3

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.v3.model.AppInfo
import com.wuruoye.all2.v3.presenter.AppInfoListGet
import kotlinx.android.synthetic.main.activity_main.*
import com.wuruoye.all2.base.util.extensions.toast
import com.wuruoye.all2.v3.adapter.HomeListRVAdapter
import com.wuruoye.all2.v3.model.ListItem

class MainActivity : BaseActivity(){

    private var isNetRefresh = false
    private lateinit var appInfoListGet: AppInfoListGet
    private val mView = object : AbsView<ArrayList<AppInfo>>{
        override fun setModel(model: ArrayList<AppInfo>) {
            runOnUiThread { setRecyclerView(model, isNetRefresh) }
        }

        override fun setWorn(message: String) {
            runOnUiThread { toast(message) }
        }

    }

    private val onItemClickListener = object : HomeListRVAdapter.OnItemClickListener{
        override fun showMore(position: Int) {
            toast("show more")
        }

        override fun enterApp(item: AppInfo) {
            val bundle = Bundle()
            bundle.putParcelable("info", item)
            val intent = Intent(this@MainActivity, AppInfoActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        override fun onError(message: String) {
            toast(message)
        }

        override fun onLongClick(item: AppInfo): Boolean{
            toast(item.name)
            return true
        }

        override fun onItemClick(item: ListItem, name: String, category: String) {
            when (item.open_type){
                TYPE_ARTICLE -> {
                    if (item.category_id != "0") {
                        val bundle = Bundle()
                        bundle.putParcelable("item", item)
                        bundle.putString("name", name)
                        if (item.category_id != ""){
                            bundle.putString("category", item.category_id)
                        }else {
                            bundle.putString("category", category)
                        }
                        val intent = Intent(this@MainActivity, ArticleDetailActivity::class.java)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                }
                TYPE_URL -> {

                }
                TYPE_IMG -> {

                }
                TYPE_VIDEO -> {

                }
            }
        }
    }

    override val contentView: Int
        get() = R.layout.activity_main

    override fun initData(bundle: Bundle?) {
        appInfoListGet = AppInfoListGet(applicationContext)
        appInfoListGet.attachView(mView)
    }

    override fun initView() {
        srl_main.setOnRefreshListener { requestData(NET_REQUEST) }
        requestData(LOCAL_REQUEST)
    }

    private fun requestData(method: Int){
        if (method == NET_REQUEST){
            appInfoListGet.requestData("", "", "", AbsPresenter.Method.NET)
            isNetRefresh = true
        }else{
            appInfoListGet.requestData("", "", "", AbsPresenter.Method.LOCAL)
        }
    }

    private fun setRecyclerView(list: ArrayList<AppInfo>, isNet: Boolean){
        isNetRefresh = false
        srl_main.isRefreshing = false
        val adapter = HomeListRVAdapter(list, onItemClickListener, isNet)
        rl_main.layoutManager = LinearLayoutManager(this)
        rl_main.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        appInfoListGet.detachView()
    }

    companion object {
        val NET_REQUEST = 2
        val LOCAL_REQUEST = 1

        // 打开指定项的方式， 分别为 打开文章 打开链接 打开图片 打开视频
        val TYPE_ARTICLE = "1"
        val TYPE_URL = "2"
        val TYPE_IMG = "3"
        val TYPE_VIDEO = "4"
    }
}
