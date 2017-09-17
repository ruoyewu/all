package com.wuruoye.all2.v3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.extensions.loadImage
import com.wuruoye.all2.base.util.extensions.toast
import com.wuruoye.all2.v3.model.ArticleDetail
import com.wuruoye.all2.v3.model.ListItem
import com.wuruoye.all2.v3.model.Pair
import com.wuruoye.all2.v3.presenter.ArticleDetailGet
import kotlinx.android.synthetic.main.activity_article.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
class ArticleDetailActivity : BaseActivity() {
    private lateinit var item: ListItem
    private lateinit var name: String
    private lateinit var category: String

    private lateinit var articleDetailGet: ArticleDetailGet
    private val mView = object : AbsView<ArticleDetail>{
        override fun setModel(model: ArticleDetail) {
            runOnUiThread { setDetail(model) }
        }

        override fun setWorn(message: String) {
            runOnUiThread { toast(message) }
        }

    }

    override val contentView: Int
        get() = R.layout.activity_article

    override fun initData(bundle: Bundle?) {
        item = bundle!!.getParcelable("item")
        name = bundle.getString("name")
        category = bundle.getString("category")
        articleDetailGet = ArticleDetailGet(applicationContext)
        articleDetailGet.attachView(mView)
    }

    override fun initView() {
        srl_article.setOnRefreshListener { requestArticle(METHOD_NET) }

        requestArticle(METHOD_LOCAL)
    }

    private fun requestArticle(method: Int){
        if (method == METHOD_LOCAL){
            articleDetailGet.requestData(name, category, item.id, AbsPresenter.Method.LOCAL)
        }else{
            articleDetailGet.requestData(name, category, item.id, AbsPresenter.Method.NET)
        }
    }

    private fun setDetail(detail: ArticleDetail){
        if (detail.title == ""){
            tv_article_title.text = item.title
        }else{
            tv_article_title.text = detail.title
        }

        if (detail.subtitle == ""){
            tv_article_subtitle.visibility = View.GONE
        }else{
            tv_article_subtitle.text = detail.subtitle
        }

        if (detail.author == ""){
            if (item.author == ""){
                tv_article_author.visibility = View.GONE
            }else{
                tv_article_author.text = item.author
            }
        }else{
            tv_article_author.text = detail.author
        }

        if (item.forward == ""){
            tv_article_forward.visibility = View.GONE
        }else{
            tv_article_forward.text = item.forward
        }

        if (detail.date == ""){
            if (item.age == ""){
                if (item.date == ""){
                    if (item.time_millis == ""){
                        tv_article_date.visibility = View.GONE
                    }else{
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = item.time_millis.toLong()
                        val date = calendar.get(Calendar.YEAR).toString() + "-" + (calendar.get(Calendar.MONTH) + 1) +
                                "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR) + ":" +
                                calendar.get(Calendar.MINUTE)
                        tv_article_date.text = date
                    }
                }else{
                    tv_article_date.text = item.date.substring(0, 16)
                }
            }else{
                tv_article_date.text = item.age
            }
        }else{
            tv_article_date.text = detail.date
        }

        setContentList(detail.content)
    }

    private fun setContentList(content: ArrayList<Pair>){
        for (i in 0 until content.size){
            val pair = content[i]
            ll_article_content.addView(
                    when (pair.type){
                        TYPE_TEXT -> {
                            val view = LayoutInflater.from(this)
                                    .inflate(R.layout.view_text, null) as TextView
                            view.text = pair.info
                            view
                        }
                        TYPE_IMG -> {
                            val view = LayoutInflater.from(this)
                                    .inflate(R.layout.view_image, null) as ImageView
                            loadImage(pair.info, view)
                            view
                        }
                        TYPE_TEXT_CEN -> {
                            val view = LayoutInflater.from(this)
                                    .inflate(R.layout.view_txt_cen, null) as TextView
                            view.text = pair.info
                            view
                        }
                        else -> {
                            val view = LayoutInflater.from(this)
                                    .inflate(R.layout.view_text, null) as TextView
                            view.text = pair.info
                            view
                        }
                    }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        articleDetailGet.detachView()
    }

    companion object {
        val METHOD_LOCAL = 1
        val METHOD_NET = 2

        val TYPE_TEXT = "1"
        val TYPE_IMG = "2"
        val TYPE_VIDEO = "3"
        val TYPE_H1 = "4"
        val TYPE_H2 = "5"
        val TYPE_LI = "6"
        val TYPE_TEXT_CEN = "7"
    }
}