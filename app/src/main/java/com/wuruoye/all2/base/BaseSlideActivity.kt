package com.wuruoye.all2.base

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wuruoye.all2.R
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
    private val maxAlpha = 255

    private var isSlideBack = true
    private var isBlackEdge = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settingCache = SettingCache(this)
        isSlideBack = settingCache.isSlideBack
        isBlackEdge = settingCache.isBlackEdge

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

    private fun initSlideLayout(view: View){
        mBackgroundView = window.decorView

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
        if (isBlackEdge) {
            mBackgroundView.setBackgroundColor(Color.BLACK)
        }
        val alpha = maxAlpha * (1 - progress)
        mBackgroundView.background.alpha = alpha.toInt()
    }

    fun getSlideLayout(): SlideLayout? = mSlideLayout
}