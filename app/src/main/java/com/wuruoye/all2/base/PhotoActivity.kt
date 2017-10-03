package com.wuruoye.all2.base

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.util.FileUtil
import com.wuruoye.all2.base.util.PermissionUtil
import android.content.pm.PackageManager
import com.wuruoye.all2.base.util.FilePathUtil
import com.wuruoye.all2.base.util.loge
import java.io.File


/**
 * Created by wuruoye on 2017/9/26.
 * this file is to do
 */
abstract class PhotoActivity : BaseActivity(){
    /**
     * 继承此类的时候需要实现的方法
     * 调用函数 choosePhoto(), takePhoto(), cropPhoto，执行成功时调用此函数
     * @param photoPath 调用相应函数得到的结果文件路径
     */
    abstract fun onPhotoBack(photoPath: String)

    //是否需要剪裁
    private var isCrop = false
    //图片的输出文件名
    private lateinit var fileName: String
    //剪裁图片长宽比
    private var aspectX = 0
    private var aspectY = 0
    //剪裁图片输出大小
    private var outputX = 0
    private var outputY = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==  CHOOSE_PHOTO && resultCode == Activity.RESULT_OK){
            //相册选取返回
            if (isCrop) {
                val filePath = FilePathUtil.getPathFromUri(applicationContext, data!!.data)!!
                val uri = FileProvider.getUriForFile(applicationContext, Config.PROVIDER_AUTHORITY, File(filePath))
                cropPhoto(uri)
            }else{
                val filePath = FilePathUtil.getPathFromUri(applicationContext, data!!.data)!!
                onPhotoBack(filePath)
            }
        }else if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            //拍照返回
            if (isCrop){
                val uri = FileProvider.getUriForFile(applicationContext, Config.PROVIDER_AUTHORITY, File(Config.FILE_PATH + fileName))
                cropPhoto(uri)
            }else{
                val filePath = Config.FILE_PATH + fileName
                onPhotoBack(filePath)
            }
        }else if (requestCode == CROP_PHOTO && resultCode == Activity.RESULT_OK){
            //剪裁返回
            val filePath = Config.FILE_PATH + fileName
            onPhotoBack(filePath)
        }
    }

    /**
     * 相册选取，需要剪裁
     * @param fileName 剪裁之后的输出文件
     * @param aX,aY aX/aY 为宽高比
     * @param oX,oY 分别为输入文件的宽高
     */
    fun choosePhoto(fileName: String, aX: Int, aY: Int, oX: Int, oY: Int){
        if (PermissionUtil(this).requestPermission(Config.FILE_PERMISSION)){
            aspectX = aX
            aspectY = aY
            outputX = oX
            outputY = oY
            this.fileName = fileName
            isCrop = true

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, CHOOSE_PHOTO)
        }
    }

    /**
     * 相册选取，不需剪裁
     */
    fun choosePhoto() {
        if (PermissionUtil(this).requestPermission(Config.FILE_PERMISSION)){
            isCrop = false

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, CHOOSE_PHOTO)
        }
    }

    /**
     * 拍照，需要剪裁
     * @param fileName 输出文件名
     * @param aX,aY 剪裁比例
     * @param oX,oY 输入文件大小
     */
    fun takePhoto(fileName: String, aX: Int, aY: Int, oX: Int, oY: Int){
        if (PermissionUtil(this).requestPermission(Config.FILE_PERMISSION) &&
                PermissionUtil(this).requestPermission(Config.CAMERA_PERMISSION)){
            isCrop = true
            this.fileName = fileName
            aspectX = aX
            aspectY = aY
            outputX = oX
            outputY = oY

            val file = FileUtil.createFile(fileName)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val uri =
                    if (Build.VERSION.SDK_INT < 21){
                        Uri.fromFile(file)
                    }else{
                        FileProvider.getUriForFile(applicationContext, Config.PROVIDER_AUTHORITY, file)
                    }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(intent, TAKE_PHOTO)
        }
    }

    /**
     * 拍照，不需剪裁
     * @param fileName 输出文件名
     */
    fun takePhoto(fileName: String){
        if (PermissionUtil(this).requestPermission(Config.FILE_PERMISSION) &&
                PermissionUtil(this).requestPermission(Config.CAMERA_PERMISSION)){
            isCrop = false
            this.fileName = fileName

            val file = FileUtil.createFile(fileName)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val uri =
                    if (Build.VERSION.SDK_INT < 21){
                        Uri.fromFile(file)
                    }else{
                        FileProvider.getUriForFile(applicationContext, Config.PROVIDER_AUTHORITY, file)
                    }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(intent, TAKE_PHOTO)
        }
    }

    /**
     * 剪裁图片
     * @param filePath 需要剪裁的图片
     * @param fileName 剪裁之后的输出文件
     * @param aX,aY,oX,oY 同上
     */
    fun cropPhoto(filePath: String, fileName: String, aX: Int, aY: Int, oX: Int, oY: Int){
        this.fileName = fileName
        aspectX = aX
        aspectY = aY
        outputX = oX
        outputY = oY
        val uri = FileProvider.getUriForFile(applicationContext, Config.PROVIDER_AUTHORITY, File(filePath))
        cropPhoto(uri)
    }

    private fun cropPhoto(uri: Uri){
        if (PermissionUtil(this).requestPermission(Config.FILE_PERMISSION)){
            val file = FileUtil.createFile(fileName)
            val outUri = FileProvider.getUriForFile(applicationContext, Config.PROVIDER_AUTHORITY, file)
            val intent = Intent("com.android.camera.action.CROP")
            intent.setDataAndType(uri, "image/*")
            intent.putExtra("crop", true)
            intent.putExtra("aspectX", aspectX)
            intent.putExtra("aspectY", aspectY)
            intent.putExtra("outputX", outputX)
            intent.putExtra("outputY", outputY)
            intent.putExtra("return-data", false)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri)
            //申请文件读写权限
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

            /**
             * 获得所有能执行此intent的应用，并为它们申请权限
             */
            val resInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                loge(resolveInfo.activityInfo.packageName)
                val packageName = resolveInfo.activityInfo.packageName
                grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                grantUriPermission(packageName, outUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivityForResult(intent, CROP_PHOTO)
        }
    }

    companion object {
        val CHOOSE_PHOTO = 1
        val TAKE_PHOTO = 2
        val CROP_PHOTO = 3
    }
}