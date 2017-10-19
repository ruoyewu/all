package com.wuruoye.all2.base.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.ScrollView
import com.wuruoye.all2.base.util.loge

/**
 * Created by wuruoye on 2017/10/9.
 * this file is to do
 */

class SlideRelativeLayout : RelativeLayout {
    // 手指按下的初始位置
    private var startY = 0F
    private var startX = 0F
    // 是否正在执行关闭页面的动画
    // 如果为 true 不接受其他手势
    private var isClosing = false
    // 是否正在执行返回初始位置动画， 手指触摸时强制改为 false
    // 如果为 false 不再执行 返回 动画
    private var isBacking = false

    private var isFirstMove = true

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

    private lateinit var backAnimatorX: ValueAnimator
    private lateinit var backAnimatorY: ValueAnimator
    private lateinit var closeAnimatorX: ValueAnimator
    private lateinit var closeAnimatorY: ValueAnimator

    // 初始化 各个animator
    private fun init(){
        backAnimatorX = ValueAnimator()
        backAnimatorX.addUpdateListener { animation ->
            val value = animation!!.animatedValue as Float
            if (isBacking){
                translationX = value
                onSlideListener?.translatePage(value / measuredWidth)
            }else{
                backAnimatorX.cancel()
            }
        }

        backAnimatorY = ValueAnimator()
        backAnimatorY.addUpdateListener { animation ->
            val value = animation!!.animatedValue as Float
            if (isBacking){
                translationY = value
                onSlideListener?.translatePage(value / measuredHeight)
            }else{
                backAnimatorY.cancel()
            }
        }


        closeAnimatorX = ValueAnimator()
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

        closeAnimatorY = ValueAnimator()
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

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var handle = false
        when (ev!!.action){
            MotionEvent.ACTION_DOWN -> {
                startX = ev.rawX
                startY= ev.rawY
//                loge("down: $startX , $startY")
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetX = ev.rawX - startX
                val offsetY = ev.rawY - startY
//                loge("move: $offsetX , $offsetY")
                if (childType == ChildType.SCROLLVIEW){
                    val child = getChildAt(0) as ScrollView
                    if (slideType == SlideType.VERTICAL){
                        val maxScrollY = child.getChildAt(0).height - child.height
                        val scrollY = child.scrollY
                        loge("scrollView vertical $scrollY , $maxScrollY")
                        if ((scrollY == 0 && offsetY > 0) || (scrollY == maxScrollY && offsetY < 0)) {
                            handle = true
                        }
                    }else if (slideType == SlideType.HORIZONTAL){
//                        loge("scrollView horizontal $offsetX , $offsetY")
                        if (Math.abs(offsetX) > Math.abs(offsetY)){
                            handle = true
                        }
                    }else {
                        throw IllegalArgumentException("childType must be SlideType.VERTICAL or SlideType.HORIZONTAL")
                    }
                }else if (childType == ChildType.VIEWPAGER){
                    if (slideType == SlideType.HORIZONTAL) {
//                        loge("viewpager horizontal: $offsetX , $offsetY")
                        val size = mChildViewPager.childCount
                        val current = mChildViewPager.currentItem
                        if (size == 1 && Math.abs(offsetX) > Math.abs(offsetY)){
                            handle = true
                        }else if (current == 0 && offsetX > 0 && Math.abs(offsetX) > Math.abs(offsetY)){
                            handle = true
                        }else if (current == size - 1 && offsetX < 0 && Math.abs(offsetX) > Math.abs(offsetY)){
                            handle = true
                        }
                    }else {
                        throw IllegalArgumentException("childType must be SlideType.HORIZONTAL")
                    }
                }else if (childType == ChildType.PHOTOVIEW){
                    if (slideType == SlideType.VERTICAL){
                        if (Math.abs(offsetX) < Math.abs(offsetY)){
                            handle = true
                        }
                    }else {
                        throw IllegalArgumentException("childType must be SlideType.VERTICAL")
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                loge("up")
                eventUp()
            }
        }
        if (handle){
            return true
        }
        return super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action){
            MotionEvent.ACTION_MOVE -> {
                if (isFirstMove){
                    startX = event.rawX
                    startY = event.rawY
                    isFirstMove = false
                }
                val offsetX = event.rawX - startX
                val offsetY = event.rawY - startY
//                loge("touch offset : $offsetX , $offsetY")
                if (slideType == SlideType.HORIZONTAL){
                    translationX = offsetX
                }else if (slideType == SlideType.VERTICAL){
                    translationY = offsetY
                }
            }
            MotionEvent.ACTION_UP -> {
                isFirstMove = true
                eventUp()
            }
        }
        return true
    }

    private fun eventUp() {
        val offsetX = translationX
        val offsetY = translationY
        mCurrentSlideType = SlideType.NONE
        if (!isClosing){
            when (slideType){
                SlideType.HORIZONTAL -> {
                    val maxLength = width / 3
                    if (offsetX > maxLength || offsetX < -maxLength){
                        isClosing = true
                        closePage()
                    }else{
                        backToStart()
                    }
                }
                SlideType.VERTICAL -> {
                    val maxLength = height / 4
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
        fun translatePage(progress: Float)
    }

    constructor(context: Context) : super(context) {init()}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {init()}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {init()}
}
