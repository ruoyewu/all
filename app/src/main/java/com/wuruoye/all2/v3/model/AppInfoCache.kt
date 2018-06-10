package com.wuruoye.all2.v3.model

import android.content.Context
import com.google.gson.Gson
import com.wuruoye.all2.base.model.BaseCache
import com.wuruoye.all2.setting.model.bean.ManageredApp
import com.wuruoye.all2.v3.model.bean.AppInfo
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

    fun getAlAppMap(): HashMap<String, AppInfo>{
        val appMap = HashMap<String, AppInfo>()
        val data = getString(AL_APP_LIST, "[]")
        val array = JSONArray(data)
        for (i in 0 until array.length()){
            val obj = array.getJSONObject(i)
            val name = obj.getString("name")
            appMap.put(name, Gson().fromJson(obj.toString(), AppInfo::class.java))
        }
        return appMap
    }

    fun getManageredApp(): ArrayList<ManageredApp> {
        val result = getString(MANAGER_APP, "")
        val appList = ArrayList<ManageredApp>()
        if (result.isEmpty()) {
            val appMap = getAlAppMap()
            appMap.entries.forEach {
                appList.add(ManageredApp(it.value.icon, it.value.title, true))
            }
            putManageredApp(appList)
        }else {
            val array = JSONArray(result)
            (0 until array.length()).mapTo(appList) {
                Gson().fromJson(array.getString(it), ManageredApp::class.java)
            }
        }
        return appList
    }

    fun putManageredApp(appList: ArrayList<ManageredApp>) {
        val result = Gson().toJson(appList)
        setString(MANAGER_APP, result)
    }

    fun putApi(name: String, map: HashMap<String, String>){
        val key = APP_API + name
        val string = Gson().toJson(map)
        setString(key, string)
    }

    fun getApi(name: String): HashMap<String, String>?{
        val key = APP_API + name
        val string = getString(key, "")
        return if (string == ""){
            null
        }else{
            Gson().fromJson(string, HashMap<String, String>()::class.java)
        }
    }


    companion object {
        val INFO_APP = "info_app_"
        val INFO_APP_DEFAULT = ""
        val APP_ICON = "app_logo_"

        val AV_APP_LIST = "av_app_list"
        val AL_APP_LIST = "al_app_list"
        val APP_API = "app_api_"
        val MANAGER_APP = "manager_app"
    }
}