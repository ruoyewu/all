package com.wuruoye.all2.base

import android.app.Application
import android.content.Context

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    companion object {
        private var mContext: Context? = null

        fun getApplication(): Context? = mContext
    }
}