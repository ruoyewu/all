package com.wuruoye.all2.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import com.google.gson.Gson
import com.wuruoye.all2.R
import com.wuruoye.all2.base.util.DateUtil
import com.wuruoye.all2.base.util.SQLiteUtil
import com.wuruoye.all2.base.util.toast
import com.wuruoye.all2.user.model.UserCache
import com.wuruoye.all2.user.model.bean.ArticleFavorite
import com.wuruoye.all2.user.presenter.UserGet
import com.wuruoye.all2.user.presenter.UserView
import com.wuruoye.all2.v3.model.bean.ArticleListItem
import kotlinx.android.synthetic.main.fragment_user_course.*

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
class UserCourseFragment : RefreshFragment() {
    private lateinit var mUserCache: UserCache
    private lateinit var mUserGet: UserGet

    private val mView = object : UserView{
        override fun onFavoriteGet(model: ArticleFavorite) {

        }

        override fun onAvatarUpload(result: Boolean) {

        }

        override fun onReadTimeUpload(result: Boolean) {
            activity?.runOnUiThread {
                toast("同步成功")
                val activity = activity as UserActivity
                activity.stopRefresh()
            }
        }

        override fun setWorn(message: String) {
            activity?.runOnUiThread {
                toast(message)
            }
        }

    }
    override val contentView: Int
        get() = R.layout.fragment_user_course

    override fun initData(bundle: Bundle?) {
        mUserCache = UserCache(context!!)
        mUserGet = UserGet(context!!)
        mUserGet.attachView(mView)
    }

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        tv_course_read_time.text = "已阅读时间:\t${DateUtil.getTime(mUserCache.readTime)}"
        val lastRead = mUserCache.lastRead
        val title =
                if (lastRead != ""){
                    Gson().fromJson(lastRead, ArticleListItem::class.java).title
                }else {
                    "暂无"
                }
        tv_course_read_last.text = "上次阅读:\t$title"
    }

    override fun refresh() {
        mUserGet.uploadReadTime(mUserCache.userId, mUserCache.readTime)
    }
}