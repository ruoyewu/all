package com.wuruoye.all2.base.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.wuruoye.all2.R
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */

fun toast(message: String){
//    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    Toast.show(message)
}

fun Context.loadImage(url: String, imageView: ImageView){
    Glide.with(this)
            .load(url)
            .into(imageView)
}

fun Context.loadUserImage(userName: String, imageView: ImageView){
    val url = Config.USER_AVATAR_URL + "/" + userName
    val option = RequestOptions()
            .placeholder(R.drawable.ic_avatar)
            .error(R.drawable.ic_avatar)
    Glide.with(this)
            .load(url)
            .apply(option)
            .into(imageView)
}


fun Context.getImageBitmap(url: String, listener: Listener<Bitmap>){
    Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : SimpleTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                    if (resource != null){
                        listener.onSuccess(resource)
                    }
                }

            })
}

fun Context.clearGlide(){
    Glide.get(this).clearMemory()
    Thread(Runnable { Glide.get(this).clearDiskCache() }).start()
}

fun loge(message: Any){
    Log.e("zhangqun", message.toString())
}