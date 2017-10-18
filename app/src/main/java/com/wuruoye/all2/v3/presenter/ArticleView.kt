package com.wuruoye.all2.v3.presenter

import com.wuruoye.all2.base.presenter.BaseView
import com.wuruoye.all2.v3.model.ArticleComment
import com.wuruoye.all2.v3.model.ArticleCommentItem
import com.wuruoye.all2.v3.model.ArticleDetail
import com.wuruoye.all2.v3.model.ArticleInfo

/**
 * Created by wuruoye on 2017/9/23.
 * this file is to do
 */

interface ArticleView : BaseView{
    fun onArticleDetail(model: ArticleDetail)
    fun onArticleError()
    fun onArticleInfo(model: ArticleInfo)
    fun onCommentGet(model: ArticleComment)
    fun onCommentPut(model: ArticleCommentItem)
    fun onCommentDelete(model: Boolean)
    fun onCommentReport(model: Boolean)
    fun onLovePut(model: Boolean)
    fun onFavoritePut(model: Boolean)
    fun setWorn(message: String)
}
