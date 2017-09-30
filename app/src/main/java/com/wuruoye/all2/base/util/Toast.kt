package com.wuruoye.all2.base.util

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.wuruoye.all2.R
import com.wuruoye.all2.base.App

/**
 * Created by wuruoye on 2017/9/30.
 * this file is to do
 */
object Toast {

    fun show(message: String){
        show(message, Toast.LENGTH_SHORT)
    }

    fun show(message: String, show_length: Int){
        val context = App.getApplication()
        val toast = Toast(context)
        val view = LayoutInflater.from(context)
                .inflate(R.layout.view_toast, null)
        val tv = view.findViewById<TextView>(R.id.tv_toast)
        tv.setOnClickListener {
            loge("toast click")
        }
        tv.text = message
        toast.duration = show_length
        toast.view = view
        toast.show()
    }
}