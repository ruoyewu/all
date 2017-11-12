package com.wuruoye.all2.base.presenter

import android.content.Context
import org.json.JSONObject
import java.lang.ref.WeakReference

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
@Suppress("UNCHECKED_CAST")
abstract class AbsPresenter<out V : BaseView> : BasePresenter {
    private var mViewRef: WeakReference<V>? = null

    enum class Method{
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

    protected fun checkResponse(response: String): Pair<Boolean, String>{
        val obj = JSONObject(response)
        val result = obj.getBoolean("result")
        if (result){
            return Pair(result, obj.getString("content"))
        }else {
            return Pair(result, obj.getString("info"))
        }
    }
}