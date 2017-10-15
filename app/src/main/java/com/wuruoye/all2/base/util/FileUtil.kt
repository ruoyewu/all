package com.wuruoye.all2.base.util

import android.graphics.Bitmap
import com.wuruoye.all2.base.model.Config
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * Created by wuruoye on 2017/9/26.
 * this file is to do
 */
object FileUtil {

    fun createFile(fileName: String): File{
        checkDirectory()

        return File(Config.FILE_PATH + fileName)
    }

    fun saveImage(bitmap: Bitmap, fileName: String): String{
        checkDirectory()

        val file = File(Config.IMAGE_PATH + fileName)
        val fos = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()

        return file.canonicalPath
    }

    fun writeFile(fileName: String, inStream: InputStream){
        checkDirectory()

        val file = Config.FILE_PATH + fileName
        val fos = FileOutputStream(file)
        val buf = ByteArray(1024)
        var len: Int

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
        val file = File(Config.FILE_PATH)
        val image = File(Config.IMAGE_PATH)

        if (!file.exists()){
            file.mkdirs()
        }
        if (!image.exists()){
            image.mkdirs()
        }
    }
}