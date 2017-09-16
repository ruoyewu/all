package com.wuruoye.all2.v3.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.wuruoye.all2.base.model.BaseCache
import org.json.JSONArray

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
class AppInfoCache(context: Context) : BaseCache(context) {

    var infoList: String
        get() = getString(INFO_LIST, INFO_LIST_DEFAULT)
        set(value) = setString(INFO_LIST, value)


    fun setAppInfo(info: String, name: String){
        val key = INFO_APP + name
        setString(key, info)
    }

    fun getAppInfo(name: String): String{
        val key = INFO_APP + name
        return getString(key, INFO_APP_DEFAULT)
    }

    companion object {
        val INFO_LIST = "info_list"
        val INFO_LIST_DEFAULT = ""
        val INFO_APP = "info_app_"
        val INFO_APP_DEFAULT = ""
    }
}