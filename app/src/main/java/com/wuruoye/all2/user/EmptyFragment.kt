package com.wuruoye.all2.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_empty.*

/**
 * Created by wuruoye on 2017/9/29.
 * this file is to do
 */
class EmptyFragment : BaseFragment() {
    override val contentView: Int
        get() = R.layout.fragment_empty

    override fun initData(bundle: Bundle?) {

    }

    override fun initView(view: View) {
        btn_fragment_empty.setOnClickListener {
            val activity = activity as UserActivity
            activity.startToLogin()
        }
    }
}