package com.wuruoye.all2.v3

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.github.chrisbanes.photoview.PhotoView
import com.transitionseverywhere.ChangeText
import com.transitionseverywhere.TransitionManager
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.util.*
import com.wuruoye.all2.base.widget.SlideRelativeLayout
import com.wuruoye.all2.v3.adapter.ViewVPAdapter
import kotlinx.android.synthetic.main.activity_image.*

/**
 * Created by wuruoye on 2017/10/8.
 * this file is to do
 */

class ImageActivity : BaseActivity() {
    private lateinit var imageList: ArrayList<String>
    private var position = 0

    private lateinit var imageDialog: AlertDialog

    override val contentView: Int
        get() = R.layout.activity_image

    override fun initData(bundle: Bundle?) {
        imageList = bundle!!.getStringArrayList("images")
        position = bundle.getInt("position")
    }

    override fun initView() {
        overridePendingTransition(R.anim.activity_open_bottom, R.anim.activity_no)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        initDialog()

        activity_image.setOnSlideListener(object : SlideRelativeLayout.OnSlideListener{
            override fun translatePage(progress: Float) {

            }
            override fun onClosePage() {
                finish()
                overridePendingTransition(R.anim.activity_no, R.anim.activity_no)
            }

        })

        val viewList = ArrayList<View>()
        for (i in 0 until imageList.size){
            val image = imageList[i]
            val view = PhotoView(this)
            view.maximumScale = 5F
            view.setOnPhotoTapListener { _, _, _ -> imageDialog.show() }
            view.setOnOutsidePhotoTapListener { imageDialog.show() }
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
        setCurrentPager(position)
    }

    fun setCurrentPager(position: Int){
        TransitionManager.beginDelayedTransition(activity_image, ChangeText())
        try {
            ArticleDetailActivity.EventManager.sendEvent(position)
        } catch (e: Exception) {
            loge("no detail activity to manager")
        }
        val text = "${position + 1} / ${imageList.size}"
        tv_image_num.text = text
    }

    private fun initDialog(){
        imageDialog = AlertDialog.Builder(this)
                .setItems(dialog_items, { _, position ->
                    when (position){
                        0 -> {
                            val imageUrl = imageList[vp_image.currentItem]
                            val imageName = imageUrl.split('/').last()
                            getImageBitmap(imageUrl, object : Listener<Bitmap>{
                                override fun onSuccess(model: Bitmap) {
                                    Thread({
                                        val filePath = FileUtil.saveImage(model, imageName)
                                        runOnUiThread { toast(filePath) }
                                    }).start()
                                }

                                override fun onFail(message: String) {
                                    toast(message)
                                }

                            })
                        }
                        1 -> {
                            val imageUrl = imageList[vp_image.currentItem]
                            val imageName = imageUrl.split('/').last()
                            getImageBitmap(imageUrl, object : Listener<Bitmap>{
                                override fun onSuccess(model: Bitmap) {
                                    Thread({
                                        val filePath = FileUtil.saveImage(model, imageName)
                                        ShareUtil.shareImage(filePath, this@ImageActivity)
                                    }).start()
                                }

                                override fun onFail(message: String) {
                                    toast(message)
                                }

                            })
                        }
                        2 -> {
                            copyText(imageList[vp_image.currentItem])
                        }
                        else -> {}
                    }
                })
                .create()

    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.activity_no, R.anim.activity_close_bottom)
    }

    companion object {
        val dialog_items = arrayOf(
                "保存图片", "分享图片", "复制图片地址"
        )
    }
}
