package com.nice.baselibrary.base.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * String工具类
 * @author JPlus
 * @date 2019/1/23.
 */
class StringUtils {
    companion object {
        private val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

        /**
         * 解析URL中的文件名
         */
        fun parseUrlName(url: String): String {
            val tempUrl = url.substring(url.lastIndexOf("/") + 1)
            return tempUrl.substring(0, if (tempUrl.indexOf("?") == -1) tempUrl.length else tempUrl.indexOf("?"))
        }


        /**
         * long转文件大小
         * @param size
         * @return 返回B、K、M、G
         */
        fun parseByteSize(size: Long): String {
            return if (size in 0..1023) {
                String.format("%dB", size)
            } else if (size >= 1024 && size < 1024 * 1024) {
                String.format("%dK", size / 1024)
            } else if (size >= 1024 * 1024 && size < 1024 * 1024 * 1024) {
                String.format("%.2fM", size / 1024.0 / 1024.0)
            } else if (size >= 1024 * 1024 * 1024) {
                String.format("%.2fG", size / 1024.0 / 1024.0 / 1024.0)
            } else {
                "无法获取"
            }
        }

        /**
         * 字节数组转为字符串
         * @param bytes
         * @return String
         */
        fun byte2HexString(bytes: ByteArray): String {
            val len = bytes.size
            if (len == 0) return ""
            val ret = CharArray(len.shl(1))
            var j = 0
            for (i in bytes) {
                ret[j++] = hexDigits[i.toInt().shr(4) and 0x0f]
                ret[j++] = hexDigits[i.toInt() and 0x0f]
            }
            return String(ret)
        }


    }

}