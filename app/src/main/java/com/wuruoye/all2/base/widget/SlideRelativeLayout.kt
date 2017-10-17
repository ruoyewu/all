package com.wuruoye.all2.base.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.wuruoye.all2.base.util.loge

/**
 * Created by wuruoye on 2017/10/9.
 * this file is to do
 */

class SlideRelativeLayout : RelativeLayout {
    private val maxLength = 300F
    private val DIRECT_NONE = 0
    private val DIRECT_UP = 1
    private val DIRECT_DOWN = 2
    private val DIRECT_LEFT = 3
    private val DIRECT_RIGHT = 4

    // 手指按下的初始位置
    private var startY = 0F
    private var startX = 0F
    // 是否正在执行关闭页面的动画
    // 如果为 true 不接受其他手势
    private var isClosing = false
    // 是否正在执行返回初始位置动画， 手指触摸时强制改为 false
    // 如果为 false 不再执行 返回 动画
    private var isBacking = false

    private var isManager = true

    // 当前view 设置的需要滑动方向 (HORIZONTAL, VERTICAL)
    var slideType = SlideType.VERTICAL
    // 当前view 的子view类型，用于判断不同的滑动冲突方案
    var childType = ChildType.PHOTOVIEW
    // 当前滑动的方向, 手指放开重设为 none， 再根据下一次滑动时 offsetX 与 offsetY 的大小区别
    private var mCurrentSlideType = SlideType.NONE

    private lateinit var mChildViewPager: ViewPager

    // 滑动方向
    enum class SlideType{
        NONE,
        HORIZONTAL,
        VERTICAL
    }
    // 子布局类型，不同子布局执行不同策略
    enum class ChildType{
        PHOTOVIEW,
        SCROLLVIEW,
        VIEWPAGER
    }

    private var onSlideListener: OnSlideListener? = null

    private lateinit var backAnimatorX: ObjectAnimator
    private lateinit var backAnimatorY: ObjectAnimator
    private lateinit var closeAnimatorX: ObjectAnimator
    private lateinit var closeAnimatorY: ObjectAnimator

    // 初始化 各个animator
    private fun init(){
        backAnimatorX = ObjectAnimator()
        backAnimatorX.addUpdateListener { animation ->
            val value = animation!!.animatedValue as Float
            if (isBacking){
                translationX = value
                onSlideListener?.translatePage(value / measuredWidth)
            }else{
                backAnimatorX.cancel()
            }
        }

        backAnimatorY = ObjectAnimator()
        backAnimatorY.addUpdateListener { animation ->
            val value = animation!!.animatedValue as Float
            if (isBacking){
                translationY = value
                onSlideListener?.translatePage(value / measuredHeight)
            }else{
                backAnimatorY.cancel()
            }
        }


        closeAnimatorX = ObjectAnimator()
        closeAnimatorX.addUpdateListener { animation ->
            val value = animation!!.animatedValue as Float
            if (isClosing){
                translationX = value
                onSlideListener?.translatePage(value / measuredWidth)
            }else{
                closeAnimatorX.cancel()
            }
        }
        closeAnimatorX.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                onSlideListener?.onClosePage()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })

        closeAnimatorY = ObjectAnimator()
        closeAnimatorY.addUpdateListener { animation ->
            val value = animation!!.animatedValue as Float
            if (isClosing){
                translationY = value
                onSlideListener?.translatePage(value / measuredHeight)
            }else{
                closeAnimatorY.cancel()
            }
        }
        closeAnimatorY.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                onSlideListener?.onClosePage()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        loge("dispatch")
        if (!isClosing) {
            when (ev!!.action){
                MotionEvent.ACTION_DOWN -> {
                    isManager = true
                    isBacking = false
                    startY = ev.rawY
                    startX = ev.rawX
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isManager) {
                        val offsetX = ev.rawX - startX
                        val offsetY = ev.rawY - startY
                        loge("offsetX: $offsetX, offsetY: $offsetY")
                        if (childType == ChildType.SCROLLVIEW){
                            if (slideType == SlideType.VERTICAL) {
                                val child = getChildAt(0) as ViewGroup
                                val maxScrollY = child.getChildAt(0).height - child.height
                                val scrollY = child.scrollY
                                if ((offsetY >= 0 && scrollY == 0) || (offsetY <= 0 && scrollY == maxScrollY)){
//                                    if (checkSlideType(ev)) {
//                                        return true
//                                    }
                                    return checkSlideType(ev)
                                }else{
                                    isManager = false
                                }
                            }else if (slideType == SlideType.HORIZONTAL){
//                                if (checkSlideType(ev)) {
//                                    return true
//                                }
                                return checkSlideType(ev)
                            }else{
                                throw IllegalArgumentException("slideType must not be SlideType.NONE")
                            }
                        }else if (childType == ChildType.VIEWPAGER){
                            if (slideType == SlideType.HORIZONTAL){
                                val size = mChildViewPager.adapter.count
                                val position = mChildViewPager.currentItem
    //                            loge("viewpager: size: $size, position: $position")
                                if (size == 1){
//                                    if (checkSlideType(ev)) {
//                                        return true
//                                    }
                                    return checkSlideType(ev)
                                }else if (position == 0){
//                                    if (checkSlideType(ev, DIRECT_LEFT)) {
//                                        return true
//                                    }
                                    return checkSlideType(ev, DIRECT_LEFT)
                                }else if (position == size - 1){
//                                    if (checkSlideType(ev, DIRECT_RIGHT)) {
//                                        return true
//                                    }
                                    return checkSlideType(ev, DIRECT_RIGHT)
                                }
                            }else{
                                throw IllegalArgumentException("slideType must be SlideType.HORIZONTAL if childType is ChileType.VIEWPAGER")
                            }
                        }
                    }
                }
                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_UP -> {
                    eventUp()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//        loge("intercept")
        when (ev!!.action){
            MotionEvent.ACTION_MOVE -> {
                if (childType == ChildType.PHOTOVIEW && slideType == SlideType.VERTICAL){
                    checkSlideType(ev)
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        loge("on touch")
        return true
    }

    private fun checkSlideType(ev: MotionEvent): Boolean{
        return checkSlideType(ev, DIRECT_NONE)
    }

    private fun checkSlideType(ev: MotionEvent, forbidden: Int): Boolean{
        var isSlide = false
        val offsetY = ev.rawY - startY
        val offsetX = ev.rawX - startX
        if (mCurrentSlideType == SlideType.NONE){
            mCurrentSlideType =
                    when {
                        Math.abs(offsetX) > Math.abs(offsetY) -> SlideType.HORIZONTAL
                        Math.abs(offsetY) > Math.abs(offsetX) -> SlideType.VERTICAL
                        else -> SlideType.NONE
                    }
        }
//        loge("check: offsetX: $offsetX, offsetY: $offsetY, type: $mCurrentSlideType, forbidden: $forbidden")
        when (slideType){
            SlideType.HORIZONTAL -> {
                if (mCurrentSlideType == SlideType.HORIZONTAL){
                    if ( (offsetX > 0 && forbidden != DIRECT_RIGHT) || (offsetX < 0 && forbidden != DIRECT_LEFT)){
                        translationX = offsetX
                        onSlideListener?.translatePage(offsetX / measuredWidth)
                        isSlide = true
                    }
                }
            }
            SlideType.VERTICAL -> {
                if (mCurrentSlideType == SlideType.VERTICAL){
                    if ( (offsetY > 0 && forbidden != DIRECT_DOWN) || (offsetY < 0 && forbidden != DIRECT_UP)) {
                        translationY = offsetY
                        onSlideListener?.translatePage(offsetY / measuredHeight)
                        isSlide = true
                    }
                }
            }
            else -> {isSlide = true}
        }
        return isSlide
    }

    private fun eventUp() {
        val offsetX = translationX
        val offsetY = translationY
//        loge("end: x: $offsetX, y: $offsetY")
        mCurrentSlideType = SlideType.NONE
        if (!isClosing){
            when (slideType){
                SlideType.HORIZONTAL -> {
                    if (offsetX > maxLength || offsetX < -maxLength){
                        isClosing = true
                        closePage()
                    }else{
                        backToStart()
                    }
                }
                SlideType.VERTICAL -> {
                    if (offsetY > maxLength || offsetY < -maxLength){
                        isClosing = true
                        closePage()
                    }else{
                        backToStart()
                    }
                }
                else -> {}
            }
        }
    }

    private fun closePage() {
        onSlideListener?.isClosingPage()

        val offsetX = translationX
        val offsetY = translationY
        val width = if (offsetX > 0) measuredWidth else -measuredWidth
        val height = if (offsetY > 0) measuredHeight else -measuredHeight
        closeAnimatorX.setFloatValues(offsetX, width.toFloat())
        closeAnimatorY.setFloatValues(offsetY, height.toFloat())
        when (slideType){
            SlideType.VERTICAL -> {
                closeAnimatorY.start()
            }
            SlideType.HORIZONTAL -> {
                closeAnimatorX.start()
            }
            else -> {}
        }
    }

    private fun backToStart() {
        val offsetX = translationX
        val offsetY = translationY
        isBacking = true
        backAnimatorX.setFloatValues(offsetX, 0F)
        backAnimatorY.setFloatValues(offsetY, 0F)
        when (slideType){
            SlideType.VERTICAL -> {
                backAnimatorY.start()
            }
            SlideType.HORIZONTAL -> {
                backAnimatorX.start()
            }
            else -> {}
        }
     }

    fun setOnSlideListener(listener: OnSlideListener){
        this.onSlideListener = listener
    }

    fun attachViewPager(viewPager: ViewPager){
        this.mChildViewPager = viewPager
    }

    interface OnSlideListener{
        fun onClosePage()
        fun isClosingPage()
        fun translatePage(progress: Float)
    }

    constructor(context: Context) : super(context) {init()}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {init()}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {init()}
}
