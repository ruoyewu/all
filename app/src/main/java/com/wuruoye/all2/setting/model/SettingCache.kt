package com.wuruoye.all2.setting.model

import android.content.Context
import com.wuruoye.all2.base.model.BaseCache

/**
 * Created by wuruoye on 2017/10/15.
 * this file is to do
 */
class SettingCache(context: Context) : BaseCache(context) {

    var isAutoMainButton: Boolean
        get() = getBoolean(SETTING_MAIN_BUTTON, true)
        set(value) = setBoolean(SETTING_MAIN_BUTTON, value)

    var isAutoDetailButton: Boolean
        get() = getBoolean(SETTING_DETAIL_BUTTON, true)
        set(value) = setBoolean(SETTING_DETAIL_BUTTON, value)

    var isSlideBack: Boolean
        get() = getBoolean(SETTING_SLIDE_BACK, true)
        set(value) = setBoolean(SETTING_SLIDE_BACK, value)

    var isBlackEdge: Boolean
        get() = getBoolean(SETTING_BLACK_EDGE, true)
        set(value) = setBoolean(SETTING_BLACK_EDGE, value)

    companion object {
        val SETTING_MAIN_BUTTON = "main_button"
        val SETTING_DETAIL_BUTTON = "detail_button"
        val SETTING_SLIDE_BACK = "slide_back"
        val SETTING_BLACK_EDGE = "black_edge"
    }
}