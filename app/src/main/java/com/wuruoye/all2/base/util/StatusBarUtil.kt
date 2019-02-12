package com.wuruoye.all2.base.util

import android.content.Context

/**
 * @Created : wuruoye
 * @Date : 2018/6/14 14:55.
 * @Description : 状态栏工具类
 */
object StatusBarUtil {
    fun getHeight(context: Context): Int {
        var height = 0
        val resourceId = context.resources.getIdentifier("status_bar_height",
                "dimen", "android")
        if (resourceId > 0) {
            height = context.resources.getDimensionPixelSize(resourceId)
        }
        return height
    }
}