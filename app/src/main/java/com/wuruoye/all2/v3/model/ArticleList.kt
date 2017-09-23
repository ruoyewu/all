package com.wuruoye.all2.v3.model

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
data class ArticleList(
        var name: String,
        var category: String,
        var next: String,
        var list: ArrayList<ArticleListItem>
)