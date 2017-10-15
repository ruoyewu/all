package com.wuruoye.all2.v3.model

import android.content.Context
import com.google.gson.Gson
import com.wuruoye.all2.base.model.BaseCache
import org.json.JSONArray

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
class AppInfoCache(context: Context) : BaseCache(context) {

    fun setAppInfo(info: String, name: String){
        val key = INFO_APP + name
        setString(key, info)
    }

    fun getAppInfo(name: String): String{
        val key = INFO_APP + name
        return getString(key, INFO_APP_DEFAULT)
    }

    fun putAppIcon(name: String, value: String){
        val key = APP_ICON + name
        setString(key, value)
    }

    fun getAppIcon(name: String): String{
        val key = APP_ICON + name
        return getString(key, "")
    }

    fun getAvAppList(): ArrayList<String>{
        val string = getString(AV_APP_LIST, "[]")
        val list = Gson().fromJson(string, ArrayList<String>()::class.java)
        return list
    }

    fun putAvAppList(list: ArrayList<String>){
        val string = Gson().toJson(list)
        setString(AV_APP_LIST, string)
    }

    fun putAlAppList(data: String){
        setString(AL_APP_LIST, data)
    }

    fun getAlAppList(): HashMap<String, AppInfo>{
        val appMap = HashMap<String, AppInfo>()
        val data = getString(AL_APP_LIST, "[]")
        val array = JSONArray(data)
        for (i in 0 until array.length()){
            val obj = array.getJSONObject(i)
            val name = obj.getString("name")
            appMap.put(name, Gson().fromJson(array.getString(i), AppInfo::class.java))
        }
        return appMap
    }

    companion object {
        val INFO_APP = "info_app_"
        val INFO_APP_DEFAULT = ""
        val APP_ICON = "app_logo_"

        val AV_APP_LIST = "av_app_list"
        val AL_APP_LIST = "al_app_list"
    }
}