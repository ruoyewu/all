package com.wuruoye.all2.v3.model

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
data class ListItem(
        var id: String,
        var title: String,
        var forward: String,
        var author: String,
        var image: String,
        var video: String,
        var date: String,
        var time_millis: String,
        var age: String,
        var original_url: String,
        var type: String,
        var category_id: String,
        var other_info: String,
        var open_type: String,
        var content: ArrayList<Pair>
)