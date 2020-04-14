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
fun Context.uri2Path(uri: Uri, selection: String?): String {
    LogUtils.d("uri2Path:$uri")
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
 * @param path 文件路径
 * @return Uri 转换的uri
 */
@Throws(IllegalArgumentException::class)
fun Context.path2Uri(path: String, authority: String): Uri {
    LogUtils.d("path2Uri:$path")
    return if (getApiLevel() >= Build.VERSION_CODES.N) {
        //android 7.0 开始只能使用provider获取uri
        FileProvider.getUriForFile(this, authority, File(path))
    } else {
        Uri.fromFile(File(path))
    }
}

/**
 * 解析系统uri并返回文件路径
 * @param uri
 * @return String
 */
fun Context.parseUri(uri: Uri): String {
    if (DocumentsContract.isDocumentUri(this, uri)) {
        val docId = DocumentsContract.getDocumentId(uri)
        if ("com.android.providers.media.documents" == uri.authority) {
            val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val selection = MediaStore.Images.Media._ID + "=" + id
            return uri2Path(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
        } else if ("com.android.providers.downloads.documents" == uri.authority) {
            val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    docId.toLong())
            return uri2Path(contentUri, null)
        }
    } else if ("content".equals(uri.scheme, true)) {
        return if ("com.google.android.apps.photos.content" == uri.authority) {
            uri.lastPathSegment ?: ""
        } else {
            uri2Path(uri, null)
        }
    } else if ("file".equals(uri.scheme, true)) {
        return uri.path ?: ""
    }
    return ""
}
