package com.nice.baselibrary.base.utils

import android.app.Activity
import android.content.ContentUris
import android.database.CursorIndexOutOfBoundsException
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File

/**
 * @author JPlus
 * @date 2019/11/23.
 */
object UriUtils {
    /**
     * 通过uri获取文件的原本路径
     * @param uri
     * @param selection
     * @return String
     */
    @Throws(CursorIndexOutOfBoundsException::class)
     fun getPathByUri(uri: Uri, selection: String?, activity:Activity): String {
        LogUtils.instance.d("getPathByUri:$uri")
        var path = ""
        val cursor = activity.contentResolver.query(uri, null, selection, null, null)
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
     fun getUriByPath(filePath: String, activity:Activity): Uri {
        LogUtils.instance.d("getUriByPath:$filePath")
        return  if ( AppUtils.instance.getApiLevel() >= Build.VERSION_CODES.N) {
            //android 7.0 开始只能使用provider获取uri
            FileProvider.getUriForFile(activity,  activity.application.packageName + ".provider", File(filePath))
        } else {
            Uri.fromFile(File(filePath))
        }
    }

    /**
     * 解析系统返回的uri
     * @param uri
     * @return String
     */
     fun parseUri(uri: Uri, activity:Activity): String {
        var imgPath = ""
        uri.let {
            if (DocumentsContract.isDocumentUri(activity, it)) {
                val docId = DocumentsContract.getDocumentId(it)
                if ("com.android.providers.media.documents" == it.authority) {
                    val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    val selection = MediaStore.Images.Media._ID + "=" + id
                    imgPath = getPathByUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, activity)
                } else if ("com.android.providers.downloads.documents" == it.authority) {
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            docId.toLong())
                    imgPath = getPathByUri(contentUri, null, activity)
                }
            } else if ("content".equals(it.scheme, true)) {
                imgPath = if("com.google.android.apps.photos.content" ==it.authority){
                    it.lastPathSegment?:""
                }else {
                    getPathByUri( it, null, activity)
                }
            } else if ("file".equals(it.scheme, true)) {
                imgPath = it.path?:""
            }
            LogUtils.instance.d("imgPath:$imgPath")
        }

        return  imgPath
    }

}