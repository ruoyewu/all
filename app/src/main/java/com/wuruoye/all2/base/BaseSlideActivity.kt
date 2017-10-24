package com.wuruoye.all2.base

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wuruoye.all2.R
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
    abstract val slideType: SlideLayout.SlideType
    abstract val childType: SlideLayout.ChildType
    abstract val initAfterOpen: Boolean

    private var mSlideLayout: SlideLayout? = null
    private lateinit var mBackgroundView: View
    private lateinit var mPreView: View
    private val maxAlpha = 255

    private var isSlideBack = true
    private var isBlackEdge = true
    private var isPreSlide = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mSettingCache = SettingCache(this)
        isSlideBack = mSettingCache.isSlideBack
        isBlackEdge = mSettingCache.isBlackEdge
        isPreSlide = mSettingCache.isPreSlide

        SlideHelper.addActivity(this)
        if (SlideHelper.activityList.size < 2){
            isSlideBack = false
        }

        val view = LayoutInflater.from(this)
                .inflate(contentView, null)

        if (isSlideBack) {
            initSlideLayout(view)
            setContentView(mSlideLayout)
            overridePendingTransition(R.anim.activity_no, R.anim.activity_no)
        }else {
            setContentView(view)
        }

        initData(intent.extras)

        if (isSlideBack && initAfterOpen){
            mSlideLayout!!.post {
                mSlideLayout!!.openPage()
                mSlideLayout!!.visibility = View.VISIBLE
            }
        }else {
            if (isSlideBack){
                mSlideLayout!!.post {
                    mSlideLayout!!.openPage()
                    mSlideLayout!!.visibility = View.VISIBLE
                }
            }
            initView()
        }
    }

    override fun onBackPressed() {
        if (isSlideBack) {
            mSlideLayout!!.closePage()
        }else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        SlideHelper.removeActivity(this)
        super.onDestroy()
    }

    private fun initSlideLayout(view: View){
        mBackgroundView = window.decorView
        mPreView = SlideHelper.getPreActivityView()

        mSlideLayout = SlideLayout(this)
        mSlideLayout!!.visibility = View.INVISIBLE
        mSlideLayout!!.childType = childType
        mSlideLayout!!.slideType = slideType

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
                if (initAfterOpen) {
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
            if (slideType == SlideLayout.SlideType.HORIZONTAL){
                offset = - (progress / absProgress) * (mPreView.width / 2) * (1 - absProgress)
                mPreView.translationX = offset
            }else if (slideType == SlideLayout.SlideType.VERTICAL){
                offset = - (progress / absProgress) * (mPreView.width / 2) * (1 - absProgress)
                mPreView.translationY = offset
            }
        }
    }

    fun getSlideLayout(): SlideLayout? = mSlideLayout
}