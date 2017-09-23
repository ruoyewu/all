package com.wuruoye.all2.v3.presenter

import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.v3.model.ArticleComment
import com.wuruoye.all2.v3.model.ArticleCommentItem
import com.wuruoye.all2.v3.model.ArticleDetail
import com.wuruoye.all2.v3.model.ArticleInfo

/**
 * Created by wuruoye on 2017/9/23.
 * this file is to do
 */

abstract class ArticleView : AbsView<String>{
    abstract fun onArticleDetail(model: ArticleDetail)
    abstract fun onArticleInfo(model: ArticleInfo)
    abstract fun onCommentGet(model: ArticleComment)
    abstract fun onCommentPut(model: ArticleCommentItem)
    abstract fun onCommentDelete(model: Boolean)
    abstract fun onCommentReport(model: Boolean)
    abstract fun onLovePut(model: Boolean)
}
