package com.wuruoye.all2.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.wuruoye.all2.base.util.FontUtil

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        mContext = this

        FontUtil.changeFont(this, "fonts/longzhao.ttf")
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mContext: Context? = null

        fun getApplication(): Context? = mContext
    }
}