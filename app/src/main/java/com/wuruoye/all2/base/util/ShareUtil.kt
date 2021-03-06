package com.wuruoye.all2.base.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.content.FileProvider
import com.wuruoye.all2.base.model.Config
import java.io.File

/**
 * Created by wuruoye on 2017/10/14.
 * this file is to do
 */

object ShareUtil{
    fun shareImage(imagePath: String, context: Context){
        val file = File(imagePath)
        val uri = FileProvider.getUriForFile(context, Config.PROVIDER_AUTHORITY, file)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        val resInfoList = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            loge(resolveInfo.activityInfo.packageName)
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(intent)
    }
}
