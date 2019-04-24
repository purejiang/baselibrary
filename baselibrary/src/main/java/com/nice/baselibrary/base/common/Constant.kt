package com.nice.baselibrary.base.common

import android.os.Environment
import java.io.File


/**
 * 全局静态常量
 * @author JPlus
 * @date 2019/2/22.
 */
class Constant {


        class Api{

        }

        /**
         *
         */
         class Message{
             companion object {
                 val DEFAULT_APP_NAME = "Application"
             }

        }

        /**
         * 全局路径常量
         */
        class Path {
            companion object {
                val ROOT_DIR = Environment.getExternalStoragePublicDirectory("").absolutePath
                val CRASH_INFO_DIR = "crash"
                val LOGCAT_INFO_DIR = "logcat"
                val DOWNLOAD_PATH = "download"
                val PATCH_DEX_PATH = "patch"
            }
        }

        /**
         * 持久化相关常量
         */
        class Persistence {
            companion object {
                val DEFAULT = "setting"
            }
        }


}
