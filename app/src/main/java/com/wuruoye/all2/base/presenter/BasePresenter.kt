package com.wuruoye.all2.base.presenter

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
interface BasePresenter {
    fun attachView(view: BaseView)
    fun detachView()
}