package com.wuruoye.all2.v3.model

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
data class AppInfo(
        var name: String,
        var title: String,
        var icon: String,
        var category_name: ArrayList<String>,
        var category_title: ArrayList<String>
)