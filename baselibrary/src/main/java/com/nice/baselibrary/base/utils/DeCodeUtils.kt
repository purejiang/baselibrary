package com.nice.baselibrary.base.utils

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * 编码解码工具类
 * @author JPlus
 * @date 2019/3/26.
 */

class DeCodeUtils {
        /**
         * url编码
         * @param content
         * @param charsetName
         * @return
         */
        @Throws(UnsupportedEncodingException::class)
        fun urlEncode(content:String, charsetName:String ="UTF-8"):String{
             return if(content.isEmpty()) URLEncoder.encode(content, charsetName) else ""
        }

        /**
         * url解码
         * @param content
         * @param charsetName
         * @return
         */
        @Throws(UnsupportedEncodingException::class)
        fun urlDecode(content:String, charsetName:String ="UTF-8"):String{
            return if(content.isEmpty()) URLDecoder.decode(content, charsetName) else ""
        }

        /**
         * base64编码
         * @param byteArray
         * @return
         */
        fun base64Eecode(byteArray:ByteArray):ByteArray{
            return if(byteArray.isEmpty()) Base64.encode(byteArray, Base64.NO_WRAP) else byteArrayOf()
        }

        /**
         * base64编码
         * @param byteArray
         * @return
         */
        fun base64Eecode2String(byteArray:ByteArray):String{
            return if(byteArray.isEmpty()) Base64.encodeToString(byteArray, Base64.NO_WRAP) else ""
        }

        /**
         * base64解码
         * @param content
         * @return
         */
        fun base64Decode(content:String):ByteArray{
            return if(content.isEmpty()) Base64.decode(content, Base64.NO_WRAP) else byteArrayOf()
        }

}