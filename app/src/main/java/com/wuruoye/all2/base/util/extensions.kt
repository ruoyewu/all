package com.wuruoye.all2.base.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.wuruoye.all2.R
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.v3.ArticleDetailActivity
import com.wuruoye.all2.v3.ImageActivity
import com.wuruoye.all2.v3.MainActivity
import com.wuruoye.all2.v3.model.ArticleListItem

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */

fun toast(message: String){
//    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    Toast.show(message)
}

fun Context.loadImage(url: String, imageView: ImageView){
    val option = RequestOptions()
    Glide.with(this)
            .load(url)
            .apply(option)
            .into(imageView)
}

fun Context.loadUserImage(userName: String, imageView: ImageView){
    val url = Config.USER_AVATAR_URL + "/" + userName
    val option = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
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

fun Context.loadUrl(url: String){
    val intent = Intent(Intent.ACTION_VIEW)
    val uri = Uri.parse(url)
    intent.data = uri
    Intent.createChooser(intent, "选择打开软件: ")
    startActivity(intent)
}

fun Context.clearGlide(){
    Glide.get(this).clearMemory()
    Thread(Runnable { Glide.get(this).clearDiskCache() }).start()
}

fun Context.startActivity(item: ArticleListItem, name: String, category: String){
    when (item.open_type){
        OPEN_TYPE_ARTICLE -> {
            if (item.category_id != "0") {
                val bundle = Bundle()
                bundle.putParcelable("item", item)
                bundle.putString("name", name)
                if (item.category_id != ""){
                    bundle.putString("category", item.category_id)
                }else {
                    bundle.putString("category", category)
                }
                val intent = Intent(this, ArticleDetailActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
        OPEN_TYPE_URL -> {

        }
        OPEN_TYPE_IMG -> {
            val imgList = item.img_list
            val position = 0
            val bundle = Bundle()
            bundle.putStringArrayList("images", imgList)
            bundle.putInt("position", position)
            val intent = Intent(this, ImageActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        OPEN_TYPE_VIDEO -> {

        }
    }
}

fun loge(message: Any){
    Log.e("zhangqun", message.toString())
}

fun String.toUp(): String =
        this.replace("1", "一")
                .replace("2", "二")
                .replace("3", "三")
                .replace("4", "四")
                .replace("5", "五")
                .replace("6", "六")
                .replace("7", "七")
                .replace("8", "八")
                .replace("9", "九")
                .replace("0", "零")

fun Int.toUpString(){
    val string = this.toString()
    val size = string.length
    for (i in size downTo 0){

    }
}

val N2S = arrayOf(
        "", "十", "百", "千", "万", "十", "百", "千"
)

val OPEN_TYPE_ARTICLE = "1"
val OPEN_TYPE_URL = "2"
val OPEN_TYPE_IMG = "3"
val OPEN_TYPE_VIDEO = "4"