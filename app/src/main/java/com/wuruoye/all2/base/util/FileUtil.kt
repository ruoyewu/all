package com.wuruoye.all2.base.util

import com.wuruoye.all2.base.model.Config
import java.io.File

/**
 * Created by wuruoye on 2017/9/26.
 * this file is to do
 */
object FileUtil {

    fun createFile(fileName: String): File{
        val direct = Config.FILE_PATH + "file/"
        val directory = File(direct)
        if (!directory.exists()){
            directory.mkdirs()
        }

        return File(direct + fileName)
    }
}