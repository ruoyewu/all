package com.wuruoye.all2.v3

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.extensions
import com.wuruoye.all2.v3.model.AppInfo
import com.wuruoye.all2.v3.presenter.AppInfoListGet
import kotlinx.android.synthetic.main.activity_main.*
import com.wuruoye.all2.base.util.extensions.toast
import com.wuruoye.all2.v3.adapter.HomeListRVAdapter

class MainActivity : BaseActivity(){

    private lateinit var appInfoListGet: AppInfoListGet
    private val mView = object : AbsView<ArrayList<AppInfo>>{
        override fun setModel(model: ArrayList<AppInfo>) {
            runOnUiThread { setRecyclerView(model) }
        }

        override fun setWorn(message: String) {
            runOnUiThread { toast(message) }
        }

    }

    private val onItemClickListener = object : HomeListRVAdapter.OnItemClickListener{
        override fun showMore(position: Int) {
            toast("show more")
        }

        override fun enterApp(position: Int) {
            toast("click " + position)
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
        }else{
            appInfoListGet.requestData("", "", "", AbsPresenter.Method.LOCAL)
        }
    }

    private fun setRecyclerView(list: ArrayList<AppInfo>){
        srl_main.isRefreshing = false
        val adapter = HomeListRVAdapter(list, onItemClickListener)
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
    }
}
