package com.wuruoye.all2.user

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.wuruoye.all2.R
import com.wuruoye.all2.base.App
import com.wuruoye.all2.base.RefreshFragment
import com.wuruoye.all2.base.util.extensions.toast

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
class UserCollectFragment : RefreshFragment(){

    override val contentView: Int
        get() = R.layout.fragment_user_collect

    override fun initData(bundle: Bundle?) {

    }

    override fun initView(view: View) {

    }

    override fun refresh() {
        Handler().postDelayed({
            try {
                val activity = activity as UserActivity
                activity.stopRefresh()
            } catch (e: Exception) {
                App.getApplication()?.toast("刷新时请不要离开页面...")
            }
        }, 2000)
    }
}