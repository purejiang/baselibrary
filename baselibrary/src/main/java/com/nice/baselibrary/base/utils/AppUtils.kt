package com.nice.baselibrary.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import com.nice.baselibrary.base.common.Constant
import com.leniu.assist.ln.entity.AppInfo
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 * @author Administrator
 * @date 2019/2/22.
 */
class AppUtils {
    companion object {
        private var mAppUtils:AppUtils?=null
        @Synchronized
        fun getInstance():AppUtils{
            if(mAppUtils==null){
                mAppUtils = AppUtils()
            }
            return mAppUtils!!
        }
    }

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
                return Constant.Companion.Message.DEFAULT_APP_NAME
            }
            return packageManager.getApplicationLabel(applicationInfo) as String
        }

        /**
         * 获取手机已安装的非系统应用
         * @param context
         * @param isSystem
         * @return
         */
        fun getAppsInfo(isSystem: Boolean,context: Context = mContext!!): MutableList<AppInfo> {
            val appsInfo: MutableList<AppInfo> = ArrayList()
            val packageList: List<PackageInfo> = context.packageManager.getInstalledPackages(0)
            for (packageInfo in packageList) {
                val term: Boolean = if (!isSystem) {
                    (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0
                } else {
                    (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                }

                if (term) {
                    val info: ApplicationInfo = context.packageManager.getApplicationInfo(packageInfo.packageName, 0)
                    val date= Date(packageInfo.firstInstallTime)
                    val sp= SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val appInfo = AppInfo(
                            appName = packageInfo.applicationInfo.loadLabel(context.packageManager).toString(),
                            packageName = packageInfo.packageName,
                            versionCode = packageInfo.versionCode,
                            versionName = packageInfo.versionName,
                            icon = info.loadIcon(context.packageManager),
                            inTime= sp.format(date),
                            signMd5 = MD5Utils.encryptionMD5(context.packageManager.getPackageInfo(
                                    packageInfo.packageName, PackageManager.GET_SIGNATURES).signatures[0].toByteArray()),
                            permission = getPermissions(packageInfo.packageName, context.packageManager)
                    )
                    appsInfo.add(appInfo)
                }
            }
            return appsInfo
        }

        /**
         * 获取指定包名的应用的权限列表
         * @param packageName
         * @param packageManager
         * @return
         */
        private fun getPermissions(packageName:String, packageManager: PackageManager): MutableList<String>{
            val pi:PackageInfo=packageManager.getPackageInfo(packageName,PackageManager.GET_PERMISSIONS)
            var permissions: MutableList<String> = mutableListOf("此应用无权限信息")
            if(pi.requestedPermissions!=null) {
                permissions= pi.requestedPermissions.toMutableList()
            }
            return permissions
        }

        /**
         * 获取当前包名
         * @param context
         * @return
         */
        fun getPackageName(context: Context? = mContext): String{
            return context?.applicationInfo?.packageName?:""
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