package com.nice.baselibrary.base.utils

import java.io.*

/**
 * 命令行工具类
 * @author JPlus
 * @date 2019/4/2.
 */
class ExcCommand {
    companion object {
        /**
         * 执行命令行并返回字符串结果
         * @param command
         * @return
         */
        @Throws(IOException::class)
        @Synchronized
        fun exc(command:String):String{
            var inputStream:InputStream?=null
            val data = ByteArray(1024)
            val result = StringBuilder()
            try {
                inputStream = exc(arrayOf(command))
                var line = 0
                while ({ line = inputStream!!.read(data);line }() != -1) {
                    result.append(line)
                }
            }finally {
                try {
                    //关闭流操作
                   inputStream?.close()
                }catch (e:IOException){
                    e.printStackTrace()
                }
            }
            return result.toString()
        }

        @Throws(IOException::class)
        @Synchronized
        fun exc(command:Array<String>):InputStream{
            return Runtime.getRuntime().exec(command).inputStream
        }
    }
}