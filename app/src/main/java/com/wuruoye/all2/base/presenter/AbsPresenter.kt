package com.wuruoye.all2.base.presenter

import android.content.Context
import java.lang.ref.WeakReference

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
abstract class AbsPresenter<out V : BaseView>(
        private val context: Context
) : BasePresenter {
    private var mViewRef: WeakReference<V>? = null

    public enum class Method{
        NET,
        LOCAL
    }

    public abstract fun requestData(name: String, category: String, data: String, method: Method)

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