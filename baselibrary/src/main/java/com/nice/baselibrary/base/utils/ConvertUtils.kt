package com.nice.baselibrary.base.utils





/**
 * @author Administrator
 * @date 2019/4/2.
 */
class ConvertUtils {
    companion object {
     private val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

        /**
         * 字节数组转为字符串
         * @param bytes
         * @return String
         */
        fun byte2HexString(bytes:ByteArray):String{
         val len = bytes.size
         if(len==0)return ""
         val ret = CharArray(len.shl(1))
         var j =0
         for(i in bytes){
            ret[j++] = hexDigits[i.toInt().shr(4) and 0x0f]
             ret[j++] = hexDigits[i.toInt() and 0x0f]
         }
         return String(ret)
     }
    }
}