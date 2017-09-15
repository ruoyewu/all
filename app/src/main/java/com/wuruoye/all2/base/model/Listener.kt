package com.wuruoye.all2.base.model

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
interface Listener<T> {
    fun onSuccess(model: T)
    fun onFail(message: String)
}