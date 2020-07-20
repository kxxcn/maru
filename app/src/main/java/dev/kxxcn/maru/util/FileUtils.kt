package dev.kxxcn.maru.util

import android.content.Context
import com.google.common.base.Charsets
import java.io.ByteArrayOutputStream
import java.io.IOException

object FileUtils {

    fun getRawFile(context: Context, resource: Int): String? {
        return try {
            val data: String?
            val inputStream = context.resources.openRawResource(resource)
            val byteArrayOutputStream = ByteArrayOutputStream()
            var i: Int
            i = inputStream.read()
            while (i != -1) {
                byteArrayOutputStream.write(i)
                i = inputStream.read()
            }
            data = String(byteArrayOutputStream.toByteArray(), Charsets.UTF_8)
            inputStream.close()
            data
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
