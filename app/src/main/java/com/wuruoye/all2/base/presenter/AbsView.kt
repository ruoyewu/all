package com.wuruoye.all2.base.presenter

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
interface AbsView<T> : BaseView {
    fun setModel(model: T)
    fun setWorn(message: String)
}