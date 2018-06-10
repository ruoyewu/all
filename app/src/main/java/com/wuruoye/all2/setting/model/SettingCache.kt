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

    var isPreSlide: Boolean
        get() = getBoolean(SETTING_PRE_SLIDE, true)
        set(value) = setBoolean(SETTING_PRE_SLIDE, value)

    var isAutoImage: Boolean
        get() = getBoolean(SETTING_AUTO_IMAGE, true)
        set(value) = setBoolean(SETTING_AUTO_IMAGE, value)

    var isCircleOpen: Boolean
        get() = getBoolean(SETTING_CIRCLE_OPEN, false)
        set(value) = setBoolean(SETTING_CIRCLE_OPEN, value)

    fun getArticleGlance(name: String): Int {
        val key = ARTICLE_GLANCE + name
        return getInt(key, 0)
    }

    fun putArticleGlance(name: String, glance: Int) {
        val key = ARTICLE_GLANCE + name
        setInt(key, glance)
    }

    companion object {
        val SETTING_MAIN_BUTTON = "setting_main_button"
        val SETTING_DETAIL_BUTTON = "setting_detail_button"
        val SETTING_SLIDE_BACK = "setting_slide_back"
        val SETTING_BLACK_EDGE = "setting_black_edge"
        val SETTING_PRE_SLIDE = "setting_pre_slide"
        val SETTING_CIRCLE_OPEN = "setting_circle_open"
        val SETTING_AUTO_IMAGE = "setting_auto_image"
        val ARTICLE_GLANCE = "article_glance_"
    }
}