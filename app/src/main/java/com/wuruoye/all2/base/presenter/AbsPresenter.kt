package com.wuruoye.all2.base.presenter

import android.content.Context
import java.lang.ref.WeakReference

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
abstract class AbsPresenter<out V : BaseView> : BasePresenter {
    private var mViewRef: WeakReference<V>? = null

    public enum class Method{
        NET,
        LOCAL
    }

    override fun attachView(view: BaseView) {
        mViewRef = WeakReference<V>(view as V)
    }

    override fun detachView() {
        if (mViewRef != null){
            mViewRef!!.clear()
            mViewRef = null
        }
    }

    protected fun getView(): V?{
        return if (mViewRef != null){
            mViewRef!!.get()
        }else{
            null
        }
    }
}