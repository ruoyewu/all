package com.wuruoye.all2.v3.presenter

import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.NetUtil
import org.json.JSONObject

/**
 * Created by wuruoye on 2017/9/22.
 * this file is to do
 */
class ArticleLovePut : AbsPresenter<AbsView<Boolean>>(), Listener<Boolean> {
    override fun onSuccess(model: Boolean) {
        getView()?.setModel(model)
    }

    override fun onFail(message: String) {
        getView()?.setWorn(message)
    }

    public fun putLove(key: String, username: String, love: Boolean){
        val url = Config.ARTICLE_LOVE_URL + "key=" + key + "&username=" + username + "&love=" +
                if (love){
                    1
                }else{
                    0
                }
        NetUtil.get(url, object : Listener<String>{
            override fun onSuccess(model: String) {
                val jsonObject = JSONObject(model)
                val result = jsonObject.getBoolean("result")
                this@ArticleLovePut.onSuccess(result)
            }

            override fun onFail(message: String) {
                this@ArticleLovePut.onFail(message)
            }

        })
    }

    override fun requestData(name: String, category: String, data: String, method: Method) {

    }
}