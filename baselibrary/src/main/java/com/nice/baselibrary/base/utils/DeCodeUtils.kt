package com.nice.baselibrary.base.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * 编码解码方法集
 * @author JPlus
 * @date 2019/3/26.
 */


/**
 * url编码
 * @param content
 * @param charsetName
 * @return
 */
@Throws(UnsupportedEncodingException::class)
fun urlEncode(content: String, charsetName: String = "UTF-8"): String {
    return if (content.isNotEmpty()) URLEncoder.encode(content, charsetName) else ""
}

/**
 * url解码
 * @param content
 * @param charsetName
 * @return
 */
@Throws(UnsupportedEncodingException::class)
fun urlDecode(content: String, charsetName: String = "UTF-8"): String {
    return if (content.isNotEmpty()) URLDecoder.decode(content, charsetName) else ""
}

/**
 * base64编码
 * @param byteArray
 * @return
 */
fun base64Encode(byteArray: ByteArray): ByteArray {
    return if (byteArray.isEmpty()) Base64.encode(byteArray, Base64.NO_WRAP) else byteArrayOf()
}

/**
 * base64编码
 * @param byteArray
 * @return
 */
fun base64Encode2String(byteArray: ByteArray): String {
    return if (byteArray.isEmpty()) Base64.encodeToString(byteArray, Base64.NO_WRAP) else ""
}


/**
 * base64解码
 * @param content
 * @return
 */
fun base64Decode(content: String): ByteArray {
    return if (content.isEmpty()) Base64.decode(content, Base64.NO_WRAP) else byteArrayOf()
}


/**
 * base64转bitmap
 * @param byteArray
 * @param count 解码的次数
 * @return
 */
fun base642Bitmap(byteArray: ByteArray?, count: Int = 1): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        if (byteArray == null) return null
        var bitmapArray = byteArray
        for (i in 1..count) {
            bitmapArray = Base64.decode(bitmapArray, Base64.DEFAULT)
        }
        bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray!!.size)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bitmap
}
/**
 * bitmap转base64
 * @param file 文件
 *  @param count 编码的次数
 * @return
 */
fun bitmap2Base64(file: File?, count: Int = 1): String? {
    try {
        if (file == null) return null
        var byteArray = file.readBytes()
        for (i in 1..count) {
            byteArray = Base64.encode(byteArray, Base64.DEFAULT)
        }
        return byte2HexString(byteArray)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}
