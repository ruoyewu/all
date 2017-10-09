package com.wuruoye.all2.v3

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.github.chrisbanes.photoview.PhotoView
import com.transitionseverywhere.ChangeText
import com.transitionseverywhere.TransitionManager
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.util.loadImage
import com.wuruoye.all2.base.util.loge
import com.wuruoye.all2.base.widget.MPhotoView
import com.wuruoye.all2.v3.adapter.ViewVPAdapter
import com.wuruoye.all2.v3.widget.MViewPager
import kotlinx.android.synthetic.main.activity_image.*

/**
 * Created by wuruoye on 2017/10/8.
 * this file is to do
 */

class ImageActivity : BaseActivity() {
    private lateinit var imageList: ArrayList<String>
    private var position = 0

    override val contentView: Int
        get() = R.layout.activity_image

    override fun initData(bundle: Bundle?) {
        imageList = bundle!!.getStringArrayList("images")
        position = bundle.getInt("position")
    }

    override fun initView() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val viewList = ArrayList<View>()
        for (i in 0 until imageList.size){
            val image = imageList[i]
            val view = MPhotoView(this)
            view.maximumScale = 5F
            view.setOnPhotoTapListener { _, _, _ -> onBackPressed() }
            view.setOnOutsidePhotoTapListener { onBackPressed() }
            view.setOnPhotoListener(object : MPhotoView.OnPhotoListener{
                override fun onSlide(progress: Float) {
                    loge(progress)
                }

            })
            viewList.add(view)
            loadImage(image, view)
        }
        val adapter = ViewVPAdapter(viewList)
        vp_image.adapter = adapter
        vp_image.currentItem = position
        vp_image.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                setCurrentPager(position)
            }

        })
        vp_image.setOnSlideListener(object : MViewPager.OnViewPagerSlideListener{
            override fun onSlideOut() {
                onBackPressed()
            }

        })
        setCurrentPager(position)
    }

    fun setCurrentPager(position: Int){
        TransitionManager.beginDelayedTransition(activity_image, ChangeText())
        ArticleDetailActivity.EventManager.sendEvent(position)
        val text = "${position + 1} / ${imageList.size}"
        tv_image_num.text = text
    }
}
