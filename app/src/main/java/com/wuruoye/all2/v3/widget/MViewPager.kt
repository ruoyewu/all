package com.wuruoye.all2.v3.widget

import android.content.Context
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import com.wuruoye.all2.R
import com.wuruoye.all2.base.util.loge

/**
 * Created by wuruoye on 2017/10/8.
 * this file is to do
 */

class MViewPager : ViewPager {
    private var startY = 0F
    private val maxLength = 300F
    private var listener: OnViewPagerSlideListener? = null

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.action){
            MotionEvent.ACTION_DOWN -> {
                startY = ev.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val currentY = ev.rawY
                translationY = currentY - startY
                if (Math.abs(translationY) > maxLength){
                    listener?.onSlideOut()
                }
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                val currentY = ev.rawY
                translationY = 0F
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    fun setOnSlideListener(listener: OnViewPagerSlideListener){
        this.listener = listener
    }

    interface OnViewPagerSlideListener{
        fun onSlideOut()
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
}
