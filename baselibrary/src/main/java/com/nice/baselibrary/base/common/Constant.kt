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
         * 固定消息
         */
         class Message{
             companion object {

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
                val DOWNLOAD_DIR = "download"
                val PATCH_DEX_DIR = "patch"
                val CAMERA_DIR = "pic"
            }
        }

        /**
         * 持久化相关常量
         */
        class Persistence {
            companion object {
                val DEFAULT_APP_NAME = "Application"
                val DEFAULT = "setting"
            }
        }


}
