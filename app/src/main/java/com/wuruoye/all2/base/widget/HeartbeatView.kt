package com.wuruoye.all2.base.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.util.AttributeSet
import android.view.View

import com.wuruoye.all2.R

/**
 * Created by wuruoye on 2017/4/17.
 * this file is to do
 */

class HeartbeatView : View {

    private var color = ActivityCompat.getColor(context, R.color.mountain_mist)
    private val mDuration = 1000f
    private var mCurrent = 0f
    private var mCount = 30f
    private var mStep: Float = 0.toFloat()

    private var mCenterX: Int = 0
    private var mCenterY: Int = 0
    private lateinit var mPaint: Paint

    private val mData = FloatArray(8)
    private val mCtrl = FloatArray(16)

    private var dir: Long = 1

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = w / 2
        mCenterY = h / 2
        mStep = (mCenterX / 10).toFloat()
        initDraw()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val mPiece = mDuration / mCount

        canvas.translate(mCenterY.toFloat(), mCenterX.toFloat())
        canvas.scale(1f, -1f)

        mPaint.color = color

        val path = Path()

        path.moveTo(mData[0], mData[1])

        path.cubicTo(mCtrl[0], mCtrl[1], mCtrl[2], mCtrl[3], mData[2], mData[3])
        path.cubicTo(mCtrl[4], mCtrl[5], mCtrl[6], mCtrl[7], mData[4], mData[5])
        path.cubicTo(mCtrl[8], mCtrl[9], mCtrl[10], mCtrl[11], mData[6], mData[7])
        path.cubicTo(mCtrl[12], mCtrl[13], mCtrl[14], mCtrl[15], mData[0], mData[1])

        canvas.drawPath(path, mPaint)

        mCurrent += mPiece * dir
        if (mCurrent >= mDuration) {
            dir = -dir
        } else if (mCurrent <= 0) {
            dir = -dir
        }
        Handler().postDelayed({
            mData[1] -= mStep * 7 / mCount * dir
            mCtrl[7] += mStep * 5 / mCount * dir
            mCtrl[9] += mStep * 5 / mCount * dir
            mCtrl[4] -= mStep / mCount * dir
            mCtrl[10] += mStep / mCount * dir
            invalidate()
        }, 0L)
    }

    private fun initDraw() {
        mPaint = Paint()
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.isAntiAlias = true

        val radius = mCenterX.toFloat()
        val difference = radius * C

        mData[0] = 0f
        mData[1] = radius
        mData[2] = radius
        mData[3] = 0f
        mData[4] = 0f
        mData[5] = -radius
        mData[6] = -radius
        mData[7] = 0f

        mCtrl[0] = mData[0] + difference
        mCtrl[1] = mData[1]
        mCtrl[2] = mData[2]
        mCtrl[3] = mData[3] + difference
        mCtrl[4] = mData[2]
        mCtrl[5] = mData[3] - difference
        mCtrl[6] = mData[4] + difference
        mCtrl[7] = mData[5]
        mCtrl[8] = mData[4] - difference
        mCtrl[9] = mData[5]
        mCtrl[10] = mData[6]
        mCtrl[11] = mData[7] - difference
        mCtrl[12] = mData[6]
        mCtrl[13] = mData[7] + difference
        mCtrl[14] = mData[0] - difference
        mCtrl[15] = mData[1]
    }

    /**
     * 设置背景颜色
     * @param color 颜色的值
     */
    fun setColor(color: Int) {
        this.color = color
        invalidate()
    }

    /**
     * 设置单位时间重绘次数
     * @param count 单位时间重绘次数,值越大，跳动越慢
     */
    fun setCount(count: Int) {
        mCount = count.toFloat()
        invalidate()
    }

    companion object {
        private val C = 0.551915024494f
    }
}
