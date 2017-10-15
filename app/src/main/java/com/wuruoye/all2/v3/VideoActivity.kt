package com.wuruoye.all2.v3

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.util.toast
import com.wuruoye.videoplayer.VideoPlayer
import kotlinx.android.synthetic.main.activity_video.*

/**
 * Created by wuruoye on 2017/10/13.
 * this file is to do
 */
class VideoActivity : BaseActivity() {
    private lateinit var videoUrl: String
    private lateinit var videoTitle: String

    private var isFull = false

    override val contentView: Int
        get() = R.layout.activity_video

    override fun initData(bundle: Bundle?) {
        videoUrl = bundle!!.getString("url")
        videoTitle = bundle.getString("title")
        isFull = bundle.getBoolean("isFull")
    }

    override fun initView() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        changeOrientation()
        vp_video.setPath(videoUrl)
        vp_video.setTitle(videoTitle)
        vp_video.setWindow(window)
        vp_video.setOnVideoListener(object : VideoPlayer.OnVideoListener{
            override fun onBackClick() {
                onBackPressed()
            }

            override fun changeOrientation() {
                isFull = !isFull
                this@VideoActivity.changeOrientation()
            }

        })
        vp_video.setMenu(arrayOf("复制视频地址"), { position ->
            when (position){
                0 -> {
                    val cmb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cmb.primaryClip = ClipData.newPlainText("all", videoUrl)
                    toast("复制地址成功")
                }
                else -> {}
            }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    private fun changeOrientation() {
        requestedOrientation = if (isFull) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}