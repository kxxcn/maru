package dev.kxxcn.maru.util

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.base.Charsets
import java.io.*

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

    fun saveBitmapToCacheStorage(
        context: Context?,
        bitmap: Bitmap?,
        fileName: String,
        onComplete: ((String) -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    ) {
        if (context == null || bitmap == null) return
        val storage = context.cacheDir
        val file = File(storage, fileName)
        try {
            file.createNewFile()
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.close()
            onComplete?.invoke(file.path)
        } catch (e: Exception) {
            onFailure?.invoke()
            e.printStackTrace()
        }
    }

    /**
     * Android Q Scoped Storage 대응.
     */
    fun saveBitmapToExternalStorage(
        context: Context?,
        bitmap: Bitmap?,
        fileName: String,
        onComplete: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    ) {
        if (context == null || bitmap == null) {
            onFailure?.invoke()
            return
        }
        try {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
            }
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val item = context.contentResolver.insert(uri, values) ?: return

            context.contentResolver.openFileDescriptor(item, "w", null).use {
                val descriptor = it ?: return
                ByteArrayOutputStream().use { bos ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
                    FileOutputStream(descriptor.fileDescriptor).use { outputStream ->
                        ByteArrayInputStream(bos.toByteArray()).use { inputStream ->
                            while (true) {
                                val data = inputStream.read()
                                if (data == -1) {
                                    break
                                }
                                outputStream.write(data)
                            }
                        }
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        values.put(MediaStore.MediaColumns.IS_PENDING, 0)
                        context.contentResolver.update(item, values, null, null)
                    }
                }
            }
            onComplete?.invoke()
        } catch (e: Exception) {
            onFailure?.invoke()
            e.printStackTrace()
        }
    }

    fun checkPermission(
        activity: Activity?,
        requestCode: Int,
        vararg permissions: String,
        predicate: () -> Boolean
    ): Boolean {
        return if (activity != null && predicate() && permissions.any {
                ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_DENIED
            }) {
            ActivityCompat.requestPermissions(
                activity,
                permissions,
                requestCode
            )
            false
        } else {
            true
        }
    }
}
