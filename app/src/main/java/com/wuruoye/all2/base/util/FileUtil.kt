package com.wuruoye.all2.base.util

import com.wuruoye.all2.base.model.Config
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Created by wuruoye on 2017/9/26.
 * this file is to do
 */
object FileUtil {

    fun createFile(fileName: String): File{
        checkDirectory()

        return File(Config.FILE_PATH + fileName)
    }

    fun writeFile(fileName: String, inStream: InputStream){
        checkDirectory()

        val file = Config.FILE_PATH + fileName
        val fos = FileOutputStream(file)
        val buf = ByteArray(1024)
        var len = 0

        while (true){
            len = inStream.read(buf)
            if (len != -1){
                fos.write(buf, 0, len)
            }else{
                break;
            }
        }
        fos.flush()
        inStream.close()
    }

    private fun checkDirectory(){
        val directory = Config.FILE_PATH
        val file = File(directory)

        if (!file.exists()){
            file.mkdirs()
        }
    }
}