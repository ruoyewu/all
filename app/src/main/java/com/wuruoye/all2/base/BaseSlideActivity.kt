package com.wuruoye.all2.base

import android.animation.Animator
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.wuruoye.all2.R
import com.wuruoye.all2.base.util.loge
import com.wuruoye.all2.base.widget.SlideHelper
import com.wuruoye.all2.base.widget.SlideLayout
import com.wuruoye.all2.setting.model.SettingCache

/**
 * Created by wuruoye on 2017/10/21.
 * this file is to do
 */
abstract class BaseSlideActivity : AppCompatActivity() {
    abstract val contentView: Int
    abstract fun initData(bundle: Bundle?)
    abstract fun initView()

    private var mSlideLayout: SlideLayout? = null
    private lateinit var mBackgroundView: View
    private lateinit var mContentView: View
    private lateinit var mPreView: View
    private val maxAlpha = 200

    lateinit var mSlideType: SlideLayout.SlideType
    lateinit var mChildType: SlideLayout.ChildType
    var isInitAfterOpen: Boolean = false
    var isSlideBack = false
    private var isBlackEdge = false
    private var isPreSlide = false

    var isCircleOpe = false
    private val mPoint = Point()
    private val mPress = Point()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initThisData(intent.extras)

        SlideHelper.addActivity(this)

        initThisView()
    }

    override fun onBackPressed() {
        if (isSlideBack) {
            mSlideLayout!!.closePage()
        }else if (isCircleOpe && Build.VERSION.SDK_INT >= 21){
            val animator = ViewAnimationUtils.createCircularReveal(mContentView, mPoint.x, mPoint.y,
                    if (mContentView.height > mContentView.width) {mContentView.height.toFloat()} else {mContentView.width.toFloat()}, 0F)
            animator.duration = 500
            animator.addListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    mContentView.visibility = View.GONE
                    finish()
                    overridePendingTransition(R.anim.activity_no, R.anim.activity_no)
                }

                override fun onAnimationCancel(animation: Animator?) {
                    finish()
                    overridePendingTransition(R.anim.activity_no, R.anim.activity_no)
                }

                override fun onAnimationStart(animation: Animator?) {

                }
            })
            animator.start()
        }else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        SlideHelper.removeActivity(this)
        super.onDestroy()
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        mPress.set(event!!.x.toInt(), event.y.toInt())
        return super.dispatchTouchEvent(event)
    }

    fun startThisActivity(intent: Intent){
        val bundle = if (intent.extras == null){
            Bundle()
        }else {
            intent.extras
        }
        bundle.putParcelable("point", mPress)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun initThisData(bundle: Bundle?){
        val mSettingCache = SettingCache(this)
        isSlideBack = mSettingCache.isSlideBack
//        isSlideBack = false
        isBlackEdge = mSettingCache.isBlackEdge
        isPreSlide = mSettingCache.isPreSlide
        isCircleOpe = mSettingCache.isCircleOpen
//        isCircleOpe = true

        try {
            val point = bundle!!.getParcelable<Point>("point")
            mPoint.set(point.x, point.y)
        } catch (e: Exception) {
        }

        initData(intent.extras)
    }

    private fun initThisView(){
        mContentView = LayoutInflater.from(this)
                .inflate(contentView, null)

        if (isSlideBack) {
            initSlideLayout(mContentView)
            setContentView(mSlideLayout)
            overridePendingTransition(R.anim.activity_no, R.anim.activity_no)
            mSlideLayout!!.post {
                mSlideLayout!!.openPage()
                mSlideLayout!!.visibility = View.VISIBLE
            }
            if (!isInitAfterOpen){
                initView()
            }
        }else if (isCircleOpe && Build.VERSION.SDK_INT >= 21){
            setContentView(mContentView)
            initView()
            overridePendingTransition(R.anim.activity_no, R.anim.activity_no)
            mContentView.post {
                val animator = ViewAnimationUtils.createCircularReveal(mContentView, mPoint.x, mPoint.y, 0F,
                        if (mContentView.height > mContentView.width) {mContentView.height.toFloat()} else {mContentView.width.toFloat()})
                animator.duration = 500
                animator.start()
            }
        }else {
            setContentView(mContentView)
            initView()
        }
    }

    private fun initSlideLayout(view: View){
        mBackgroundView = window.decorView
        mPreView = SlideHelper.getPreActivityView()

        mSlideLayout = SlideLayout(this)
        mSlideLayout!!.visibility = View.INVISIBLE
        mSlideLayout!!.childType = mChildType
        mSlideLayout!!.slideType = mSlideType

        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        view.layoutParams = layoutParams

        mSlideLayout!!.setOnSlideListener(object : SlideLayout.OnSlideListener{
            override fun onClosePage() {
                finish()
                overridePendingTransition(R.anim.activity_no, R.anim.activity_no)
            }

            override fun translatePage(progress: Float) {
                onSlideProgress(progress)
            }

            override fun onOpen() {
                if (isInitAfterOpen) {
                    initView()
                }
            }

        })

        mSlideLayout!!.addView(view)
    }

    private fun onSlideProgress(progress: Float){
        val absProgress = Math.abs(progress)
        //显示黑色渐变背景
        if (isBlackEdge) {
            mBackgroundView.setBackgroundColor(Color.BLACK)
        }
        val alpha = maxAlpha * (1 - absProgress)
        mBackgroundView.background.alpha = alpha.toInt()

        //背景联动
        if (isPreSlide) {
            val offset: Float
            if (mSlideType == SlideLayout.SlideType.HORIZONTAL){
                offset = - (progress / absProgress) * (mPreView.width / 2) * (1 - absProgress)
                mPreView.translationX = offset
            }else if (mSlideType == SlideLayout.SlideType.VERTICAL){
                offset = - (progress / absProgress) * (mPreView.height / 2) * (1 - absProgress)
                mPreView.translationY = offset
            }else {
                offset = 0F
            }
            log("back offset : $offset, $progress, ${mPreView.height}")
        }
    }

    fun getSlideLayout(): SlideLayout? = mSlideLayout

    private fun log(message: String){
//        loge("BaseSlideActivity: $message")
    }
}