package com.wuruoye.all2.v3

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.PopupMenu
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import com.github.chrisbanes.photoview.PhotoView
import com.transitionseverywhere.*
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.BaseSlideActivity
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.util.*
import com.wuruoye.all2.base.widget.SlideLayout
import com.wuruoye.all2.v3.adapter.ViewVPAdapter
import kotlinx.android.synthetic.main.activity_image.*

/**
 * Created by wuruoye on 2017/10/8.
 * this file is to do
 */

class ImageActivity : BaseSlideActivity() {
    private lateinit var imageList: ArrayList<String>
    private var position = 0
    private var mIsShowTop = false

    private lateinit var imageDialog: AlertDialog
    private lateinit var dlgEditName: AlertDialog
    private lateinit var mImageMenu: PopupMenu
    private lateinit var etEditName: EditText
    private lateinit var mBp: Bitmap

    override val contentView: Int
        get() = R.layout.activity_image

    override fun initData(bundle: Bundle?) {
        imageList = bundle!!.getStringArrayList("images")
        position = bundle.getInt("position")

        mChildType = SlideLayout.ChildType.PHOTOVIEW
        mSlideType = SlideLayout.SlideType.VERTICAL
    }

    override fun initView() {
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams
//                .FLAG_FULLSCREEN)

        initDialog()

        iv_image_back.setOnClickListener { onBackPressed() }
        iv_image_menu.setOnClickListener { mImageMenu.show() }

        if (imageList.size == 1){
            tv_image_num.visibility = View.GONE
        }

        val viewList = ArrayList<View>()
        for (i in 0 until imageList.size){
            val image = imageList[i]
            val view = PhotoView(this)
            view.maximumScale = 5F
            view.setOnPhotoTapListener { _, _, _ -> showTop() }
            view.setOnOutsidePhotoTapListener { showTop() }
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
        val text = "${position + 1} / ${imageList.size}"
        tv_image_num.text = text
    }

    private fun showTop(){
        mIsShowTop = !mIsShowTop
        val set = TransitionSet()
                .addTransition(Slide(Gravity.TOP))
                .addTransition(Fade())
        TransitionManager.beginDelayedTransition(activity_image, set)
        if (mIsShowTop){
            rl_image_top.visibility = View.VISIBLE
        }else {
            rl_image_top.visibility = View.GONE
        }
    }

    @SuppressLint("RestrictedApi", "InflateParams")
    private fun initDialog(){
        imageDialog = AlertDialog.Builder(this)
                .setItems(dialog_items, { _, position ->
                    when (position){
                        0 -> {
                            val imageUrl = imageList[vp_image.currentItem]
                            getImageBitmap(imageUrl, object : Listener<Bitmap>{
                                override fun onSuccess(model: Bitmap) {
                                    val fullName = imageUrl.split("/").last()
                                    val realName = fullName.removeSuffix("." +
                                            fullName.split(".").last())
                                    etEditName.hint = realName
                                    mBp = model
                                    dlgEditName.show()
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
        mImageMenu = PopupMenu(this, iv_image_menu)
        mImageMenu.menuInflater.inflate(R.menu.image_menu, mImageMenu.menu)
        // 利用反射使菜单项能够显示icon
//        try {
//            val field = mImageMenu::class.java.getDeclaredField("mPopup")
//            field.isAccessible = true
//            val helper = field.get(mImageMenu) as MenuPopupHelper
//            helper.setForceShowIcon(true)
//        } catch (e: Exception) {
//        }
        mImageMenu.setOnMenuItemClickListener { item ->
            when (item!!.itemId){
                R.id.menu_image_save -> {
//                    val imageUrl = imageList[vp_image.currentItem]
//                    val imageName = imageUrl.split('/').last()
//                    getImageBitmap(imageUrl, object : Listener<Bitmap>{
//                        override fun onSuccess(model: Bitmap) {
//                            Thread({
//                                val filePath = FileUtil.saveImage(model, imageName)
//                                runOnUiThread {
//                                    val path = filePath.removePrefix(Environment
//                                            .getExternalStorageDirectory().absolutePath + "/")
//                                    toast(path)
//                                }
//                            }).start()
//                        }
//
//                        override fun onFail(message: String) {
//                            toast(message)
//                        }
//
//                    })
                    val imageUrl = imageList[vp_image.currentItem]
                    getImageBitmap(imageUrl, object : Listener<Bitmap>{
                        override fun onSuccess(model: Bitmap) {
                            val fullName = imageUrl.split("/").last()
                            val realName = fullName.removeSuffix("." +
                                    fullName.split(".").last())
                            etEditName.hint = realName
                            mBp = model
                            dlgEditName.show()
                        }

                        override fun onFail(message: String) {
                            toast(message)
                        }

                    })
                }
                R.id.menu_image_share -> {
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
                R.id.menu_image_copy -> {
                    copyText(imageList[vp_image.currentItem])
                }
                else -> {}
            }
            true
        }

        val view = LayoutInflater.from(this)
                .inflate(R.layout.dlg_img_name_edit, null, false)
        etEditName = view.findViewById(R.id.et_dlg_img_name_edit)
        dlgEditName = AlertDialog.Builder(this)
                .setTitle("保存文件名")
                .setView(view)
                .setPositiveButton("确定", {_, _ ->
                    val name = if (etEditName.text.isEmpty())
                        etEditName.hint.toString() else etEditName.text.toString()
                    Thread({
                        val filePath = FileUtil.saveImage(mBp, "$name.jpg")
                        runOnUiThread {
                            toast("文件已保存 $filePath")
                        }
                    }).start()
                })
                .setNegativeButton("取消", {_, _ ->

                })
                .create()
    }

    companion object {
        val dialog_items = arrayOf(
                "保存图片", "分享图片", "复制图片地址"
        )
    }
}
