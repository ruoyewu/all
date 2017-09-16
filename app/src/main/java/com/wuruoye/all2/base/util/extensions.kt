package com.wuruoye.all2.base.util

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
object extensions {

    fun Context.toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Context.loadImage(url: String, imageView: ImageView){
        Glide.with(this)
                .load(url)
                .into(imageView)
    }
}