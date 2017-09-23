package com.wuruoye.all2.v3

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import com.wuruoye.all2.base.util.extensions.loadImage
import com.wuruoye.all2.base.util.extensions.toast
import com.wuruoye.all2.v3.adapter.ArticleCommentRVAdapter
import com.wuruoye.all2.v3.adapter.viewholder.HeartRefreshViewHolder
import com.wuruoye.all2.v3.model.*
import com.wuruoye.all2.v3.presenter.ArticleGet
import com.wuruoye.all2.v3.presenter.ArticleView
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator
import kotlinx.android.synthetic.main.activity_article.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
class ArticleDetailActivity : BaseActivity() {

    // 侧边 button 集合， 用于批量管理动画
    private val viewFloatList = ArrayList<View>()

    //当前文章基本信息
    private lateinit var item: ArticleListItem
    private lateinit var name: String
    private lateinit var category: String
    private lateinit var articleKey: String

    // 侧边 button 是否在显示状态
    private var isShow = false
    // 侧边 button 是否可以点击，只有在文章加载出来之后才可以点击， true 代表文章详情已经加载出来
    private var isClick = false
    private var isLove = false
    // 编写文章评论的 dialog
    private lateinit var commentEditDialog: AlertDialog
    // 包含评论 dialog 的view
    private lateinit var commentEditView: CommentDialogView

    //用于显示正在 刷新状态 或 没有更多内容的 viewholder
    private lateinit var refreshVH: HeartRefreshViewHolder

    private lateinit var mArticleGet: ArticleGet
    private val mArticleView = object : ArticleView() {
        override fun setModel(model: String) {
            // ignore
        }

        override fun setWorn(message: String) {
            runOnUiThread { toast(message) }
        }

        override fun onArticleDetail(model: ArticleDetail) {
            runOnUiThread { setDetail(model) }
        }

        override fun onArticleInfo(model: ArticleInfo) {
            runOnUiThread { setArticleInfo(model) }
        }

        override fun onCommentGet(model: ArticleComment) {
            runOnUiThread { setComment(model) }
        }

        override fun onCommentPut(model: ArticleCommentItem) {
            runOnUiThread { insertComment(model) }
        }

        override fun onCommentDelete(model: Boolean) {
            runOnUiThread {
                if (model){
                    initComment()
                    mArticleGet.getArticleInfo(articleKey, "ruoye")
                }else{
                    toast("删除评论失败")
                }
            }
        }

        override fun onCommentReport(model: Boolean) {
            runOnUiThread {  }
        }

        override fun onLovePut(model: Boolean) {
            // 如果提交结果 与 当前状态 不相同， 则认定为提交错误，并
            if (model != isLove){
                toast("提交出错...请重试")
                setLove(model, false)
            }
        }

    }

    //文章评论列表 点击事件监听
    private val onItemClickListener = object : ArticleCommentRVAdapter.OnItemClickListener{
        override fun onLoadMore(next: Long, hv: HeartRefreshViewHolder) {
            refreshVH = hv
            requestComment(next)
        }

        override fun onItemClick(item: ArticleCommentItem) {
            this@ArticleDetailActivity.onItemClick(item)
        }

    }

    override val contentView: Int
        get() = R.layout.activity_article

    override fun initData(bundle: Bundle?) {
        item = bundle!!.getParcelable("item")
        name = bundle.getString("name")
        category = bundle.getString("category")
        articleKey = name + "_" + category + "_" + item.id

        mArticleGet = ArticleGet()
        mArticleGet.attachView(mArticleView)
    }

    override fun initView() {

        tv_article_original.setOnClickListener { openOriginal() }

        if (item.content.size > 0){
            //在 ArticleListItem 中已有文章详情的直接设置
            setData()
        }else {
            //否则 网络请求文章详情
            mArticleGet.getArticleDetail(name, category, item.id)
        }

        //将要添加动画的放到一个数组
        viewFloatList.add(cv_article_likes)
        viewFloatList.add(cv_article_comments)
        viewFloatList.add(fab_article_comment)
        viewFloatList.add(fab_article_like)

        //以下是 各种控件的监听
        fab_article_drawer.setOnClickListener {
            if (isClick) {
                isShow = !isShow
                showFAB()
            }else{
                toast("等待加载完成...")
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
            setLove(!isLove, true)
        }

        fab_article_like.setOnLongClickListener {
            toast("点击喜欢文章...")
            true
        }

        cv_article_comments.setOnClickListener {
            val height = ll_article_detail.measuredHeight
            sv_article.smoothScrollTo(0, height)
        }

        cv_article_comments.setOnLongClickListener {
            toast("点击查看评论...")
            true
        }

        cv_article_likes.setOnClickListener {
            sv_article.smoothScrollTo(0, 0)
        }

        cv_article_likes.setOnLongClickListener {
            toast("点击查看文章...")
            true
        }

        initCommentEditDialog()
    }

    //文章详情加载出来之后显示 comment 内容
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

    //初始化添加评论时的 dialog
    private fun initCommentEditDialog(){
        val view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_article_comment, null)
        commentEditView = CommentDialogView(view)
        commentEditView.btnCancel.setOnClickListener { commentEditDialog.cancel() }
        commentEditView.btnPublish.setOnClickListener { publishComment() }
        commentEditDialog = AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create()
    }

    private fun showCommentDialog(){
        showCommentDialog(0, "")
    }

    /**
     * 显示文章评论 dialog
     * @parentId 如果是对某个已有评论作出评论， parentId 为其 id， 否则默认为 0
     * @string 与 parentId 对应的 评论内容， 默认为 ""
     */
    private fun showCommentDialog(parentId: Int, string: String){
        commentEditView.et.clearFocus()
        if (parentId != 0){
            commentEditView.tvParent.visibility = View.VISIBLE
            commentEditView.tvParent.text = string
            commentEditView.tvParent.tag = parentId
        }else{
            commentEditView.tvParent.visibility = View.GONE
        }
        commentEditDialog.show()
    }

    //提交评论
    private fun publishComment(){
        if (commentEditView.et.text.toString() == ""){
            toast("评论内容不能为空...")
        }else{
            val time = System.currentTimeMillis()
            val username = "ruoye"
            val content = commentEditView.et.text.toString()
            //根据 tvParent 控件是否显示 判断 是否是对某个评论的评论
            val parent =
                    if (commentEditView.tvParent.visibility == View.VISIBLE){
                        commentEditView.tvParent.tag as Int
                    }else{
                        0
                    }
            commentEditView.et.setText("")
            mArticleGet.putComment(time, username, content,articleKey, parent)
            commentEditDialog.cancel()
        }
    }

    //侧边 button 的显示与隐藏动画
    private fun showFAB(){
        val set = TransitionSet()
                .addTransition(Fade())
                .addTransition(Scale())
                .addTransition(Slide(Gravity.BOTTOM))
        set.duration = ANIMATION_DURATION
        if (isShow) {
            set.duration = ANIMATION_DURATION + ANIMATION_DELAY * 4
            TransitionManager.beginDelayedTransition(ll_article_fab, Rotate())
            fab_article_drawer.rotation = ANIMATION_ROTATION

            set.duration = ANIMATION_DURATION
            for (i in 0 until viewFloatList.size){
                val delay = i * ANIMATION_DELAY
                Handler().postDelayed({
                    TransitionManager.beginDelayedTransition(ll_article_fab, set)
                    viewFloatList[i].visibility = View.VISIBLE
                }, delay)
            }
        }else{
            set.duration = ANIMATION_DURATION + ANIMATION_DELAY * 4
            TransitionManager.beginDelayedTransition(ll_article_fab, Rotate())
            fab_article_drawer.rotation = 0f

            set.duration = ANIMATION_DURATION
            for (i in 0 until viewFloatList.size){
                val delay = (viewFloatList.size - 1 - i) * ANIMATION_DELAY
                Handler().postDelayed({
                    TransitionManager.beginDelayedTransition(ll_article_fab, set)
                    viewFloatList[i].visibility = View.INVISIBLE
                }, delay)
            }
        }
    }

    //查看文章原网址
    private fun openOriginal(){
        toast(item.original_url)
    }

    // ArticleListItem 中已有详情时 调用此方法
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

    //请求文章评论
    private fun requestComment(next: Long){
        mArticleGet.getCommentList(articleKey, next)
    }

    //对网络请求得到的 ArticleDetail 做操作
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

    // 对网络请求得到的 ArticleInfo 做操作
    private fun setArticleInfo(info: ArticleInfo){
        isLove = info.result
        if (info.result){
            fab_article_like.setImageResource(R.drawable.ic_like_on)
        }else{
            fab_article_like.setImageResource(R.drawable.ic_like_off)
        }
        tv_article_num_like.text = info.love.toString()
        tv_article_num_comment.text = info.comment.toString()
    }

    // 对网络请求得到的 ArticleComment 做操作
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

    // 添加评论成功时插入一个 ArticleCommentItem
    private fun insertComment(item: ArticleCommentItem){
        val newNum = tv_article_num_comment.text.toString().toInt() + 1
        tv_article_num_comment.text = newNum.toString()
        val adapter = rl_article_comment.adapter as ArticleCommentRVAdapter
        adapter.addItem(item)
    }

    //获取文章详情成功时 进行的下一步操作
    private fun buttonValuable(){
        initComment()
        mArticleGet.getArticleInfo(articleKey, "ruoye")
        fab_article_drawer.show()
        isClick = true
    }

    //设置是否喜欢文章，并且上传到服务器
    private fun setLove(love: Boolean, isPut: Boolean){
        isLove = love
        if (isPut) {
            mArticleGet.putLove(articleKey, "ruoye", isLove)
        }
        if (love){
            fab_article_like.setImageResource(R.drawable.ic_like_on)
            val newNum = tv_article_num_like.text.toString().toInt() + 1
            tv_article_num_like.text = newNum.toString()
        }else{
            fab_article_like.setImageResource(R.drawable.ic_like_off)
            val newNum = tv_article_num_like.text.toString().toInt() - 1
            tv_article_num_like.text = newNum.toString()
        }
    }

    //根据 ArrayList<Pair> 设置文章内容
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

    private fun onItemClick(item: ArticleCommentItem){
        AlertDialog.Builder(this)
                .setItems(
                        if (item.username == "ruoye"){
                            COMMENT_ITEM_M
                        }else{
                            COMMENT_ITEM
                        },
                        { _, which ->
                            when (which){
                                0 -> {      //复制
                                    val cmb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    cmb.primaryClip = ClipData.newPlainText("all", item.content)
                                    toast("复制成功")
                                }
                                1 -> {      //举报

                                }
                                2 -> {      //评论
                                    showCommentDialog(item.id, item.username + ":\t" + item.content)
                                }
                                3 -> {      //删除
                                    mArticleGet.deleteComment(item.id)
                                }
                            }
                        }
                )
                .show()
    }

    override fun onDestroy() {
        super.onDestroy()

        mArticleGet.detachView()
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

        val ANIMATION_DURATION = 300L
        val ANIMATION_DELAY = 50L
        val ANIMATION_ROTATION = -135f
        val ANIMATOR_DURATION = 100L

        val COMMENT_ITEM = arrayOf(
                "复制", "举报", "评论"
        )
        val COMMENT_ITEM_M = arrayOf(
                "复制", "举报", "评论", "删除"
        )
    }
}