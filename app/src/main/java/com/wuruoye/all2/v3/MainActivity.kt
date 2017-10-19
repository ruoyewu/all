package com.wuruoye.all2.v3

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import com.transitionseverywhere.*
import com.transitionseverywhere.extra.Scale
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.*
import com.wuruoye.all2.setting.SettingActivity
import com.wuruoye.all2.setting.model.SettingCache
import com.wuruoye.all2.v3.model.bean.AppInfo
import com.wuruoye.all2.v3.presenter.AppInfoListGet
import kotlinx.android.synthetic.main.activity_main.*
import com.wuruoye.all2.user.UserActivity
import com.wuruoye.all2.user.model.UserCache
import com.wuruoye.all2.v3.adapter.HomeListRVAdapter
import com.wuruoye.all2.v3.model.AppInfoCache
import com.wuruoye.all2.v3.model.bean.ArticleListItem

class MainActivity : BaseActivity(){
    private val viewFloatList = ArrayList<View>()
    private var isShow = false
    private lateinit var appInfoCache: AppInfoCache
    private lateinit var settingCache: SettingCache

    private var isNetRefresh = false
    private lateinit var appInfoListGet: AppInfoListGet
    private val mView = object : AbsView<ArrayList<String>>{
        override fun setModel(model: ArrayList<String>) {
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

        override fun onItemClick(item: ArticleListItem, name: String, category: String) {
            startActivity(item, name, category)
        }
    }

    override val contentView: Int
        get() = R.layout.activity_main

    override fun initData(bundle: Bundle?) {
        appInfoListGet = AppInfoListGet(applicationContext)
        appInfoListGet.attachView(mView)

        appInfoCache = AppInfoCache(applicationContext)
        settingCache = SettingCache(applicationContext)
    }

    override fun initView() {
//        changeIcon()
        srl_main.setOnRefreshListener { requestData(NET_REQUEST) }
        requestData(LOCAL_REQUEST)

        viewFloatList.add(fab_main_setting)
        viewFloatList.add(fab_main_user)

        fab_main_drawer.setOnClickListener {
            isShow = !isShow
            showFab()
        }
        fab_main_user.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("username", UserCache(this).userName)
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        fab_main_setting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        if (settingCache.isAutoMainButton){
            ll_main_fab.post {
                isShow = true
                showFab()
            }
        }
    }

    private fun requestData(method: Int){
        if (method == NET_REQUEST){
            appInfoListGet.requestAppInfoList(AbsPresenter.Method.NET)
            isNetRefresh = true
        }else{
            appInfoListGet.requestAppInfoList(AbsPresenter.Method.LOCAL)
        }
    }

    private fun setRecyclerView(list: ArrayList<String>, isNet: Boolean){
        isNetRefresh = false
        srl_main.isRefreshing = false
        val adapter = HomeListRVAdapter(appInfoCache.getAlAppMap(), list, onItemClickListener, isNet, rl_main)
        rl_main.layoutManager = LinearLayoutManager(this)
        rl_main.adapter = adapter
    }

    private fun showFab(){
        val set = TransitionSet()
                .addTransition(Fade())
                .addTransition(Scale())
                .addTransition(Slide(Gravity.BOTTOM))
        set.duration = ArticleDetailActivity.ANIMATION_DURATION
        if (isShow) {
            set.duration = ArticleDetailActivity.ANIMATION_DURATION + ArticleDetailActivity.ANIMATION_DELAY * 4
            TransitionManager.beginDelayedTransition(ll_main_fab, Rotate())
            fab_main_drawer.rotation = ArticleDetailActivity.ANIMATION_ROTATION

            set.duration = ArticleDetailActivity.ANIMATION_DURATION
            for (i in 0 until viewFloatList.size){
                val delay = i * ArticleDetailActivity.ANIMATION_DELAY
                Handler().postDelayed({
                    TransitionManager.beginDelayedTransition(ll_main_fab, set)
                    viewFloatList[i].visibility = View.VISIBLE
                }, delay)
            }
        }else{
            set.duration = ArticleDetailActivity.ANIMATION_DURATION + ArticleDetailActivity.ANIMATION_DELAY * 4
            TransitionManager.beginDelayedTransition(ll_main_fab, Rotate())
            fab_main_drawer.rotation = 0f

            set.duration = ArticleDetailActivity.ANIMATION_DURATION
            for (i in 0 until viewFloatList.size){
                val delay = (viewFloatList.size - 1 - i) * ArticleDetailActivity.ANIMATION_DELAY
                Handler().postDelayed({
                    TransitionManager.beginDelayedTransition(ll_main_fab, set)
                    viewFloatList[i].visibility = View.INVISIBLE
                }, delay)
            }
        }
    }

    private fun changeIcon(){
        // TODO 修改逻辑
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
        packageManager.setComponentEnabledSetting(ComponentName(this, getTodayName()),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
    }

    private fun getTodayName(): String = packageName + weekItem[DateUtil.getWeek()]

    override fun onDestroy() {
        super.onDestroy()
        appInfoListGet.detachView()
    }

    companion object {
        val NET_REQUEST = 2
        val LOCAL_REQUEST = 1

        val weekItem = arrayOf(
                "", ".Sunday", ".Monday", ".Tuesday", ".Wednesday", ".Thursday", ".Friday", ".Saturday"
        )
    }
}
