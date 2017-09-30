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
    abstract fun onPhotoBack(photoPath: String)

    private var isCrop = false
    private lateinit var fileName: String
    private var aspectX = 0
    private var aspectY = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==  CHOOSE_PHOTO && resultCode == Activity.RESULT_OK){
            if (isCrop) {
                val filePath = FilePathUtil.getPathFromUri(applicationContext, data!!.data)!!
                val uri = FileProvider.getUriForFile(applicationContext, Config.PROVIDER_AUTHORITY, File(filePath))
                cropPhoto(uri)
            }else{
                val filePath = FilePathUtil.getPathFromUri(applicationContext, data!!.data)!!
                onPhotoBack(filePath)
            }
        }else if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            if (isCrop){
                val uri = FileProvider.getUriForFile(applicationContext, Config.PROVIDER_AUTHORITY, File(Config.FILE_PATH + fileName))
                cropPhoto(uri)
            }else{
                val filePath = Config.FILE_PATH + fileName
                onPhotoBack(filePath)
            }
        }else if (requestCode == CROP_PHOTO && resultCode == Activity.RESULT_OK){
            val filePath = Config.FILE_PATH + fileName
            onPhotoBack(filePath)
        }
    }

    fun choosePhoto(fileName: String, x: Int, y: Int){
        if (PermissionUtil(this).requestPermission(Config.FILE_PERMISSION)){
            aspectX = x
            aspectY = y
            this.fileName = fileName
            isCrop = true

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, CHOOSE_PHOTO)
        }
    }

    fun choosePhoto() {
        if (PermissionUtil(this).requestPermission(Config.FILE_PERMISSION)){
            isCrop = false

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, CHOOSE_PHOTO)
        }
    }

    fun takePhoto(fileName: String, x: Int, y: Int){
        if (PermissionUtil(this).requestPermission(Config.FILE_PERMISSION) &&
                PermissionUtil(this).requestPermission(Config.CAMERA_PERMISSION)){
            isCrop = true
            this.fileName = fileName
            aspectX = x
            aspectY = y

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

    fun cropPhoto(filePath: String){
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
            intent.putExtra("return-data", false)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            val resInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
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