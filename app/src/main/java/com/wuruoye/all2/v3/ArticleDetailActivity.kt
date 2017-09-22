package com.wuruoye.all2.v3

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.transitionseverywhere.*
import com.transitionseverywhere.extra.Scale
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.extensions.loadImage
import com.wuruoye.all2.base.util.extensions.toast
import com.wuruoye.all2.v3.adapter.ArticleCommentRVAdapter
import com.wuruoye.all2.v3.adapter.viewholder.HeartRefreshViewHolder
import com.wuruoye.all2.v3.model.*
import com.wuruoye.all2.v3.presenter.ArticleCommentGet
import com.wuruoye.all2.v3.presenter.ArticleCommentPut
import com.wuruoye.all2.v3.presenter.ArticleDetailGet
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator
import kotlinx.android.synthetic.main.activity_article.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
class ArticleDetailActivity : BaseActivity() {

    private val viewFloatList = ArrayList<View>()

    private lateinit var item: ListItem
    private lateinit var name: String
    private lateinit var category: String
    private lateinit var articleKey: String

    private var isShow = false
    private var isClick = false
    private lateinit var commentDialog: AlertDialog
    private lateinit var commentView: CommentDialogView

    private lateinit var refreshVH: HeartRefreshViewHolder

    private lateinit var articleDetailGet: ArticleDetailGet
    private val mView = object : AbsView<ArticleDetail>{
        override fun setModel(model: ArticleDetail) {
            runOnUiThread { setDetail(model) }
        }

        override fun setWorn(message: String) {
            runOnUiThread { toast(message) }
        }

    }

    private lateinit var commentGet: ArticleCommentGet
    private val mCommentGetView = object : AbsView<ArticleComment>{
        override fun setModel(model: ArticleComment) {
            runOnUiThread { setComment(model) }
        }

        override fun setWorn(message: String) {
            runOnUiThread { toast(message) }
        }
    }

    private lateinit var commentPut: ArticleCommentPut
    private val mCommentPutView = object : AbsView<ArticleCommentItem>{
        override fun setModel(model: ArticleCommentItem) {
            runOnUiThread {
                insertComment(model)
            }
        }

        override fun setWorn(message: String) {
            runOnUiThread { toast(message) }
        }

    }

    private val onItemClickListener = object : ArticleCommentRVAdapter.OnItemClickListener{
        override fun onLoadMore(next: Long, hv: HeartRefreshViewHolder) {
            refreshVH = hv
            requestComment(next)
        }

        override fun onItemClick(item: ArticleCommentItem) {
            toast(item.content + " + " + item.id)
        }

    }

    override val contentView: Int
        get() = R.layout.activity_article

    override fun initData(bundle: Bundle?) {
        item = bundle!!.getParcelable("item")
        name = bundle.getString("name")
        category = bundle.getString("category")
        articleKey = name + "_" + category + "_" + item.id

        articleDetailGet = ArticleDetailGet(applicationContext)
        commentGet = ArticleCommentGet(applicationContext)
        commentPut = ArticleCommentPut()
        articleDetailGet.attachView(mView)
        commentGet.attachView(mCommentGetView)
        commentPut.attachView(mCommentPutView)
    }

    override fun initView() {

        tv_article_original.setOnClickListener { openOriginal() }

        if (item.content.size > 0){
            setData()
        }else {
            requestArticle(METHOD_LOCAL)
        }

        viewFloatList.add(cv_article_likes)
        viewFloatList.add(cv_article_comments)
        viewFloatList.add(fab_article_comment)
        viewFloatList.add(fab_article_like)

        fab_article_drawer.setOnClickListener {
            if (isClick) {
                isShow = !isShow
                showFAB()
            }
        }

        fab_article_comment.setOnClickListener {
            showCommentDialog()
        }
        fab_article_comment.setOnLongClickListener {
            toast("点击添加评论...")
            true
        }

        fab_article_like.setOnClickListener {

        }

        fab_article_like.setOnLongClickListener {
            toast("点击喜欢文章")
            true
        }

        initCommentDialog()
    }

    private fun initComment(){
        ll_article_comment.visibility = View.VISIBLE
        val adapter = ArticleCommentRVAdapter(ArticleComment.getNullCommentList(), onItemClickListener)
        val layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean = false
        }
        layoutManager.isAutoMeasureEnabled = true
        rl_article_comment.layoutManager = layoutManager
        rl_article_comment.adapter = adapter
        val itemAnimator = FadeInLeftAnimator(OvershootInterpolator(1f))
        itemAnimator.addDuration = ANIMATOR_DURATION
        itemAnimator.removeDuration = ANIMATOR_DURATION
        rl_article_comment.itemAnimator = itemAnimator
    }

    private fun initCommentDialog(){
        val view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_article_comment, null)
        commentView = CommentDialogView(view)
        commentView.btnCancel.setOnClickListener { commentDialog.cancel() }
        commentView.btnPublish.setOnClickListener { publishComment() }
        commentDialog = AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create()
    }

    private fun showCommentDialog(){
        showCommentDialog(0, "")
    }

    private fun showCommentDialog(parentId: Int, string: String){
        commentView.et.clearFocus()
        if (parentId != 0){
            commentView.tvParent.visibility = View.VISIBLE
            commentView.tvParent.text = string
            commentView.tvParent.tag = parentId
        }else{
            commentView.tvParent.visibility = View.GONE
        }
        commentDialog.show()
    }

    private fun publishComment(){
        if (commentView.et.text.toString() == ""){
            toast("评论内容不能为空...")
        }else{
            val time = System.currentTimeMillis()
            val username = "ruoye"
            val content = commentView.et.text.toString()
            val parent =
                    if (commentView.tvParent.visibility == View.VISIBLE){
                        commentView.tvParent.tag as Int
                    }else{
                        0
                    }
            commentView.et.setText("")
            commentPut.requestCommentPut(time, username, content, articleKey, parent)
            commentDialog.cancel()
        }
    }

    private fun showFAB(){
        val set = TransitionSet()
                .addTransition(Fade())
                .addTransition(Scale())
                .addTransition(Slide(Gravity.BOTTOM))
        set.duration = ANIMATION_DURATION
        if (isShow) {
            TransitionManager.beginDelayedTransition(ll_article_fab, Rotate())
            fab_article_drawer.rotation = ANIMATION_ROTATION
            for (i in 0 until viewFloatList.size){
                val delay = i * ANIMATION_DELAY
                Handler().postDelayed({
                    TransitionManager.beginDelayedTransition(ll_article_fab, set)
                    viewFloatList[i].visibility = View.VISIBLE
                }, delay.toLong())
            }
        }else{
            TransitionManager.beginDelayedTransition(ll_article_fab, Rotate())
            fab_article_drawer.rotation = 0f
            for (i in 0 until viewFloatList.size){
                val delay = (viewFloatList.size - 1 - i) * ANIMATION_DELAY
                Handler().postDelayed({
                    TransitionManager.beginDelayedTransition(ll_article_fab, set)
                    viewFloatList[i].visibility = View.INVISIBLE
                }, delay.toLong())
            }
        }
    }

    private fun openOriginal(){
        toast(item.original_url)
    }

    private fun setData(){
        tv_article_title.text = item.title

        if (item.author == ""){
            tv_article_author.visibility = View.GONE
        }else{
            tv_article_author.text = item.author
        }

        if (item.forward == ""){
            tv_article_forward.visibility = View.GONE
        }else{
            tv_article_forward.text = item.forward
        }

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

        if (item.original_url != ""){
            tv_article_original.visibility = View.VISIBLE
        }

        setContentList(item.content)
        buttonValuable()
    }

    private fun requestArticle(method: Int){
        if (method == METHOD_LOCAL){
            articleDetailGet.requestData(name, category, item.id, AbsPresenter.Method.LOCAL)
        }else{
            articleDetailGet.requestData(name, category, item.id, AbsPresenter.Method.NET)
        }
    }

    private fun requestComment(next: Long){
        commentGet.requestComment(articleKey, next)
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

        if (item.original_url != ""){
            tv_article_original.visibility = View.VISIBLE
        }

        setContentList(detail.content)
        buttonValuable()
    }

    private fun buttonValuable(){
        initComment()
        fab_article_drawer.show()
        isClick = true
    }

    private fun setComment(model: ArticleComment){
        if (model.list.size > 0) {
            val adapter = rl_article_comment.adapter as ArticleCommentRVAdapter
            adapter.setNext(model.next)
            adapter.addItems(model.list)
        }
        if (model.list.size < 10){
            refreshVH.hv.visibility = View.GONE
            refreshVH.tv.visibility = View.VISIBLE
            refreshVH.tv.text = "快来点击右侧添加评论吧！"
        }
    }

    private fun insertComment(item: ArticleCommentItem){
        val adapter = rl_article_comment.adapter as ArticleCommentRVAdapter
        adapter.addItem(item)
    }

    @SuppressLint("InflateParams")
    private fun setContentList(content: ArrayList<Pair>){
        ll_article_content.removeAllViews()
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
                        TYPE_H1 -> {
                            val view = LayoutInflater.from(this)
                                    .inflate(R.layout.view_h1, null) as TextView
                            view.text = pair.info
                            view
                        }
                        TYPE_H2 -> {
                            val view = LayoutInflater.from(this)
                                    .inflate(R.layout.view_h2, null) as TextView
                            view.text = pair.info
                            view
                        }
                        TYPE_H3 -> {
                            val view = LayoutInflater.from(this)
                                    .inflate(R.layout.view_h3, null) as TextView
                            view.text = pair.info
                            view
                        }
                        TYPE_LI -> {
                            val view = LayoutInflater.from(this)
                                    .inflate(R.layout.view_li, null) as LinearLayout
                            val tv = view.findViewById<TextView>(R.id.tv_view_li)
                            tv.text = pair.info
                            view
                        }
                        TYPE_QUOTE -> {
                            val view = LayoutInflater.from(this)
                                    .inflate(R.layout.view_quote, null) as LinearLayout
                            val tv = view.findViewById<TextView>(R.id.tv_view_quote)
                            tv.text = pair.info
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
        commentGet.detachView()
        commentPut.detachView()
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
        val TYPE_QUOTE = "8"
        val TYPE_H3 = "9"

        val ANIMATION_DURATION = 200L
        val ANIMATION_DELAY = 80
        val ANIMATION_ROTATION = -135f
        val ANIMATOR_DURATION = 100L
    }
}