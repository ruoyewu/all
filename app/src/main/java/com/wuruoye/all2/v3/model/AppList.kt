package com.wuruoye.all2.v3.model

import com.wuruoye.all2.base.App

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
data class AppList(
        var name: String,
        var category: String,
        var next: String,
        var list: ArrayList<ListItem>
)