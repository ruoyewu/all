package com.wuruoye.all2.base.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.v4.app.ActivityCompat
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.wuruoye.all2.R

/**
 * Created by wuruoye on 2017/10/24.
 * this file is to do
 */
class ListProgress : View {
    private var mWidth = 0F
    private var mHeight = 0F

    private lateinit var mPaint: Paint
    private lateinit var mTextPaint: TextPaint
    private lateinit var mAnimator: ValueAnimator

    private var mRadius = 0F
    private var mProgressWidth = 0F
    private var mCurrentPosition = 50F
    private var mEndPosition = 0F

    private val mPathLU = Path()
    private val mPathLD = Path()
    private val mPathRU = Path()
    private val mPathRD = Path()
    private val mRect = RectF()

    private fun init(){
        initPaint()
        initAnimator()
    }

    private fun initPaint(){
        mPaint = Paint()
        mPaint.color = ActivityCompat.getColor(context, R.color.mountain_mist)
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.isAntiAlias = true

        mTextPaint = TextPaint()
        mTextPaint.color = ActivityCompat.getColor(context, R.color.romance)
        mTextPaint.isAntiAlias = true
        mTextPaint.textSize = 20F
    }

    private fun initAnimator(){
        mAnimator = ValueAnimator()
        mAnimator.interpolator = AccelerateDecelerateInterpolator()
        mAnimator.addUpdateListener { value ->
            mCurrentPosition = value.animatedValue as Float
        }
    }

    private fun initValues(width: Int, height: Int){
        this.mWidth = width.toFloat()
        this.mHeight = height.toFloat()

        mRadius = width / 4.toFloat()
        mProgressWidth = width / 3.toFloat()
    }

    private fun getCurrentPath(){
        //left
        val startXL = mWidth / 2
        mPathLU.moveTo(startXL, mCurrentPosition - mRadius * 2)
        mPathLU.rCubicTo(0F, mRadius, -mRadius, mRadius, -mRadius, mRadius * 2)
        mPathLU.rLineTo(mRadius, 0F)

        mPathLD.moveTo(startXL, mCurrentPosition + mRadius * 2)
        mPathLD.rCubicTo(0F, -mRadius, -mRadius, -mRadius, -mRadius, -mRadius * 2)
        mPathLD.rLineTo(mRadius, 0F)

        val startXR = mWidth / 2
        mPathRU.moveTo(startXR, mCurrentPosition - mRadius * 2)
        mPathRU.rCubicTo(0F, mRadius, mRadius, mRadius, mRadius, mRadius * 2)
        mPathRU.rLineTo(-mRadius, 0F)

        mPathRD.moveTo(startXR, mCurrentPosition + mRadius * 2)
        mPathRD.rCubicTo(0F, -mRadius, mRadius, -mRadius, mRadius, -mRadius * 2)
        mPathRD.rLineTo(-mRadius, 0F)
    }

    override fun onDraw(canvas: Canvas?) {
        //绘制背景边框
        mPaint.style = Paint.Style.STROKE
        val startX = (mWidth - mProgressWidth) / 2
        val endX = (mWidth + mProgressWidth) / 2
        mRect.set(startX, 0F, endX, mHeight)
        canvas?.drawRect(mRect, mPaint)

        //绘制圆的轮廓
        mPaint.style = Paint.Style.FILL_AND_STROKE
        getCurrentPath()
        canvas?.drawPath(mPathLU, mPaint)
        canvas?.drawPath(mPathLD, mPaint)
        canvas?.drawPath(mPathRU, mPaint)
        canvas?.drawPath(mPathRD, mPaint)

        canvas?.drawText("haha", mWidth / 2, mCurrentPosition, mTextPaint)

        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        initValues(w, h)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    constructor(context: Context?) : super(context){init()}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){init()}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){init()}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes){init()}
}