package com.wuruoye.all2.user

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseFragment
import com.wuruoye.all2.base.RefreshFragment

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
class UserCourseFragment : RefreshFragment() {
    override val contentView: Int
        get() = R.layout.fragment_user_course

    override fun initData(bundle: Bundle?) {

    }

    override fun initView(view: View) {

    }

    override fun refresh() {
        Handler().postDelayed({
            val activity = activity as UserActivity
            activity.stopRefresh()
        }, 1000)
    }
}