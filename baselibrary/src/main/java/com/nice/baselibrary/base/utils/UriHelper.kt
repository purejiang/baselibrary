package com.nice.baselibrary.base.utils

import android.content.ContentUris
import android.content.Context
import android.database.CursorIndexOutOfBoundsException
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File

/**
 * Uri方法集
 * @author JPlus
 * @date 2019/11/23.
 */
/**
 * 通过uri获取文件的原本路径
 * @param uri
 * @param selection
 * @return String
 */
@Throws(CursorIndexOutOfBoundsException::class)
fun Context.getPathByUri(uri: Uri, selection: String?): String {
    LogUtils.d("getPathByUri:$uri")
    var path = ""
    val cursor = this.contentResolver.query(uri, null, selection, null, null)
    if (cursor != null) {
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        }
        cursor.close()
    }
    return path
}

/**
 * 将path转换为uri
 * @param filePath
 * @return Uri
 */
@Throws(IllegalArgumentException::class)
fun Context.getUriByPath(filePath: String): Uri {
    LogUtils.d("getUriByPath:$filePath")
    return if (getApiLevel() >= Build.VERSION_CODES.N) {
        //android 7.0 开始只能使用provider获取uri
        FileProvider.getUriForFile(this, this.packageName + ".provider", File(filePath))
    } else {
        Uri.fromFile(File(filePath))
    }
}

/**
 * 解析系统返回的uri
 * @param uri
 * @return String
 */
fun Context.parseUri(uri: Uri): String {
    var imgPath = ""
    uri.let {
        if (DocumentsContract.isDocumentUri(this, it)) {
            val docId = DocumentsContract.getDocumentId(it)
            if ("com.android.providers.media.documents" == it.authority) {
                val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                imgPath = getPathByUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == it.authority) {
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        docId.toLong())
                imgPath = getPathByUri(contentUri, null)
            }
        } else if ("content".equals(it.scheme, true)) {
            imgPath = if ("com.google.android.apps.photos.content" == it.authority) {
                it.lastPathSegment ?: ""
            } else {
                getPathByUri(it, null)
            }
        } else if ("file".equals(it.scheme, true)) {
            imgPath = it.path ?: ""
        }
        LogUtils.d("imgPath:$imgPath")
    }

    return imgPath
}
