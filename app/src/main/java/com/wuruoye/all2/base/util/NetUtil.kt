package com.wuruoye.all2.base.util

import com.wuruoye.all2.base.model.Listener
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
object NetUtil {
    private val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)

            .build()!!

    fun get(url: String, listener: Listener<String>){
        val request = Request.Builder()
                .url(url)
                .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                listener.onFail(e!!.message!!)
            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response!!.isSuccessful){
                    listener.onSuccess(response.body()!!.string())
                }else{
                    listener.onFail(response.message())
                }
            }

        })
    }

    fun post(url: String, keyList: List<String>, valueList: List<String>, listener: Listener<String>){
        val requestBody = FormBody.Builder()
        for (i in 0 until keyList.size){
            requestBody.add(keyList[i], valueList[i])
        }
        val request = Request.Builder()
                .url(url)
                .post(requestBody.build())
                .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                listener.onFail(e!!.message!!)
            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response!!.isSuccessful){
                    listener.onSuccess(response.body()!!.string())
                }else{
                    listener.onFail(response.message())
                }
            }

        })
    }
}