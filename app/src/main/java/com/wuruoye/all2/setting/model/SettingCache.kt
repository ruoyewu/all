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

    companion object {
        val SETTING_MAIN_BUTTON = "main_button"
        val SETTING_DETAIL_BUTTON = "detail_button"
    }
}