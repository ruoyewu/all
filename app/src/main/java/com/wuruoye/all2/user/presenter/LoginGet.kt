package com.wuruoye.all2.user.presenter

import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.util.NetUtil
import org.json.JSONObject

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
class LoginGet : AbsPresenter<LoginView>() {

    fun loginUser(name: String, password: String){
        val url = Config.USER_LOGIN_URL + "username=" + name + "&password=" + password

        NetUtil.get(url, object : Listener<String>{
            override fun onSuccess(model: String) {
                try {
                    val jsonObject = JSONObject(model)
                    val result = jsonObject.getBoolean("result")
                    val info = jsonObject.getString("info");
                    getView()?.onLogin(result, info)
                } catch (e: Exception) {
                    getView()?.setWorn(e.message!!)
                }
            }

            override fun onFail(message: String) {
                getView()?.setWorn(message)
            }

        })
    }

    fun signUser(name: String, password: String){
        val url = Config.USER_SIGN_URL + "username=" + name + "&password=" + password

        NetUtil.get(url, object : Listener<String>{
            override fun onSuccess(model: String) {
                val jsonObject = JSONObject(model)
                val result = jsonObject.getBoolean("result")
                val info = jsonObject.getString("info")
                getView()?.onSign(result, info)
            }

            override fun onFail(message: String) {
                getView()?.setWorn(message)
            }

        })
    }
}