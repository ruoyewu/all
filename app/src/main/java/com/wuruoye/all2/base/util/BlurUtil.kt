package com.wuruoye.all2.base.util

import android.content.Context
import android.graphics.Bitmap
import android.support.v8.renderscript.Allocation
import android.support.v8.renderscript.Element
import android.support.v8.renderscript.RenderScript
import android.support.v8.renderscript.ScriptIntrinsicBlur

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
object BlurUtil {
    private val scaleRatio = 10;

    fun blurBitmap(context: Context, image: Bitmap, blurRadius: Float): Bitmap{
        val width = Math.round(image.width.toDouble() / scaleRatio).toInt()
        val height = Math.round(image.height.toDouble() / scaleRatio).toInt()

        val bitmap = Bitmap.createScaledBitmap(image, width, height, false)

        val rs = RenderScript.create(context)
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

        val input = Allocation.createFromBitmap(rs, bitmap)
        val output = Allocation.createTyped(rs, input.type)

        blurScript.setRadius(blurRadius)
        blurScript.setInput(input)
        blurScript.forEach(output)
        output.copyTo(bitmap)

        input.destroy()
        output.destroy()
        blurScript.destroy()
        rs.destroy()

        return bitmap
    }
}