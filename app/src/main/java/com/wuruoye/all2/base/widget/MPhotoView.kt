package com.wuruoye.all2.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import com.github.chrisbanes.photoview.PhotoView

/**
 * Created by wuruoye on 2017/10/8.
 * this file is to do
 */

class MPhotoView : PhotoView {
    private var startY = 0F
    private val maxLength = 200

    private var listener: OnPhotoListener? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val currentY = event.y
                translationY = currentY - startY
                val progress = Math.abs(currentY - startY) / maxLength
                listener?.onSlide(progress)
            }
            MotionEvent.ACTION_UP -> {
                val currentY = event.y
                translationY = startY - currentY
            }
        }
        return super.onTouchEvent(event)
    }

    fun setOnPhotoListener(listener: OnPhotoListener){
        this.listener = listener
    }

    interface OnPhotoListener{
        fun onSlide(progress: Float)
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attr: AttributeSet) : super(context, attr) {}
}
