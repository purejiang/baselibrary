package com.nice.baselibrary.base.common

import android.os.Environment
import java.io.File


/**
 * 全局静态常量
 * @author JPlus
 * @date 2019/2/22.
 */
class Constant {

    companion object {

        class Api{

        }

         class Message{
             companion object {
                 val DEFAULT_APPNAME = "Application"
             }

        }
        class Path {
            companion object {
                val ROOT_DIR = Environment.getExternalStoragePublicDirectory("").absolutePath + File.separator
                val CRASH_INFO_DIR =  File.separator + "crash"
                val LOGCAT_INFO_DIR =  File.separator + "logcat"
                val DOWNLOAD_PATH =  File.pathSeparator + "Download"
            }
        }
    }

}
