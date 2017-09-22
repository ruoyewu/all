package com.wuruoye.all2.v3.presenter

import android.content.Context
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.NetUtil
import org.json.JSONObject

/**
 * Created by wuruoye on 2017/9/21.
 * this file is to do
 */
class ArticleCommentPut(context: Context) : AbsPresenter<AbsView<String>>(context), Listener<String> {
    override fun onSuccess(model: String) {
        getView()?.setModel(model)
    }

    override fun onFail(message: String) {
        getView()?.setWorn(message)
    }

    fun requestCommentPut(time: Long, username: String, content: String, key: String, parent: Int){
        val url = Config.COMMENT_PUT_URL
        val keyList = arrayListOf("time", "username", "content", "key", "parent")
        val valueList = arrayListOf(time.toString(), username, content, key, parent.toString())
        NetUtil.post(url, keyList, valueList, object : Listener<String>{
            override fun onSuccess(model: String) {
                val jsonObject = JSONObject(model)
                val result = jsonObject.getBoolean("result")
                val info = jsonObject.getString("info")
                if (result){
                    this@ArticleCommentPut.onSuccess(info)
                }else{
                    this@ArticleCommentPut.onFail(info)
                }
            }

            override fun onFail(message: String) {
                this@ArticleCommentPut.onFail(message)
            }
        })
    }

    override fun requestData(name: String, category: String, data: String, method: Method) {

    }
}