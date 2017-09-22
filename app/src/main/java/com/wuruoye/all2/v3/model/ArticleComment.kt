package com.wuruoye.all2.v3.model

/**
 * Created by wuruoye on 2017/9/21.
 * this file is to do
 */
data class ArticleComment(
        var result: Boolean,
        var next: Long,
        var info: String,
        var list: ArrayList<ArticleCommentItem>
){
    companion object {
        fun getNullCommentList(): ArticleComment =
                ArticleComment(true, 0, "", arrayListOf())
    }
}