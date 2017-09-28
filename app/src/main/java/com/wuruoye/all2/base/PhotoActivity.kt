package com.wuruoye.all2.base

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.util.FilePathUtil
import com.wuruoye.all2.base.util.FileUtil
import com.wuruoye.all2.base.util.PermissionUtil

/**
 * Created by wuruoye on 2017/9/26.
 * this file is to do
 */
abstract class PhotoActivity : BaseActivity(){
    abstract fun onChoosePhoto(filePath: String)
    abstract fun onTakePhoto(filePath: String)

    private lateinit var filePath: String

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==  CHOOSE_PHOTO && resultCode == Activity.RESULT_OK){
            val filePath = FilePathUtil.getPathFromUri(applicationContext, data!!.data)
            onChoosePhoto(filePath!!)
        }else if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            onTakePhoto(filePath)
        }else if (requestCode == CROP_PHOTO && resultCode == Activity.RESULT_OK){

        }
    }

    fun choosePhoto(){
        if (PermissionUtil(this).requestPermission(Config.FILE_PERMISSION)){
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, CHOOSE_PHOTO)
        }
    }

    fun takePhoto(fileName: String){
        if (PermissionUtil(this).requestPermission(Config.FILE_PERMISSION) &&
                PermissionUtil(this).requestPermission(Config.CAMERA_PERMISSION)){
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

    }

    companion object {
        val CHOOSE_PHOTO = 1
        val TAKE_PHOTO = 2
        val CROP_PHOTO = 3
    }
}