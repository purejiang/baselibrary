package com.nice.baselibrary.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import com.nice.baselibrary.base.common.Constant

/**
 *
 * @author Administrator
 * @date 2019/2/22.
 */
class AndroidUtils {
    companion object {
        private var mContext:Context?=null
        /**
         * 初始化
         * @param context
         * @return
         */
        fun init(context: Context) {
            mContext = context.applicationContext
        }


        /**
         * 获取当前应用名称
         * @param context
         * @return
         */
        fun getApplicationName(context: Context = mContext!!): String {
            val packageManager: PackageManager
            val applicationInfo: ApplicationInfo
            try {
                packageManager = context.applicationContext.packageManager
                applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                return Constant.Companion.Message.DEFAULT_APPNAME
            }
            return packageManager.getApplicationLabel(applicationInfo) as String
        }

        /**
         * 获取当前包名
         * @param context
         * @return
         */
        fun getPackageName(context: Context? = mContext): String? {
            return context?.applicationInfo?.packageName
        }


        /**
         * 获取设备标识
         * @param context
         * @return
         */
        @SuppressLint("MissingPermission")
        fun getDeviceImei(context: Context = mContext!!): String? {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val deviceId = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                tm.imei
            } else {
                tm.deviceId
            }
            return deviceId ?: "-" // ?:左边为空才执行右边
        }
        /**
         * 获取应用版本
         * @param context
         * @return
         */

        fun getAppVersion(context: Context?= mContext): Int? {
            return try {
                val pm = mContext?.applicationContext?.packageManager
                val packageInfo = pm?.getPackageInfo(context?.packageName, 0)
                packageInfo?.versionCode ?: -1
            }catch (e:PackageManager.NameNotFoundException){
                e.printStackTrace()
                -1
            }
        }

        /**
         * 获取当前手机Android API版本
         * @return
         */
         fun getApiLevel(): Int {
            return Build.VERSION.SDK_INT
        }

        /**
         * 获取手机Android 版本
         * @return
         */
         fun getOsLevel():String{
            return "Android " + Build.VERSION.RELEASE
        }
        /**
         * 获取当前手机设备信息
         * @return
         */
         fun getDeviceInfo(): String {
            return Build.BRAND + " " + Build.MODEL
        }
    }
}