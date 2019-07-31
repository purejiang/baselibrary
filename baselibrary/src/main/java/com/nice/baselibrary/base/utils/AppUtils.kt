package com.nice.baselibrary.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.nice.baselibrary.base.common.Constant
import com.leniu.assist.ln.entity.AppInfo
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


/**
 *  App工具类
 * @author JPlus
 * @date 2019/2/22.
 */
class AppUtils {
    companion object {
        private var mAppUtils: AppUtils? = null
        fun getInstance(): AppUtils {
            if (mAppUtils == null) {
                synchronized(AppUtils::class.java) {
                    mAppUtils = AppUtils()
                }
            }
            return mAppUtils!!
        }
    }

    private var mContext: Context? = null
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
            return Constant.Persistence.DEFAULT_APP_NAME
        }
        return packageManager.getApplicationLabel(applicationInfo) as String
    }

    /**
     * 获取手机已安装的非系统应用
     * @param context
     * @param isSystem
     * @return
     */
    fun getAppsInfo(isSystem: Boolean, context: Context = mContext!!): MutableList<AppInfo> {
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
                val date = Date(packageInfo.firstInstallTime)
                val sp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val appInfo = AppInfo(
                        appName = packageInfo.applicationInfo.loadLabel(context.packageManager).toString(),
                        packageName = packageInfo.packageName,
                        versionCode = packageInfo.versionCode,
                        versionName = packageInfo.versionName,
                        icon = info.loadIcon(context.packageManager),
                        inTime = sp.format(date),
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
    private fun getPermissions(packageName: String, packageManager: PackageManager): MutableList<String> {
        val pi: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        var permissions: MutableList<String> = mutableListOf("此应用无权限信息")
        pi.requestedPermissions?.let {
            permissions = it.toMutableList()
        }
        return permissions
    }

    /**
     * 获取当前包名
     * @param context
     * @return
     */
    fun getPackageName(context: Context? = mContext): String {
        return context?.applicationInfo?.packageName ?: ""
    }


    /**
     * 获取设备标识
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    fun getDeviceImei(context: Context = mContext!!): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val deviceId = when {
            Build.VERSION.SDK_INT > Build.VERSION_CODES.P -> Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            Build.VERSION.SDK_INT > Build.VERSION_CODES.O -> try {
                tm.imei
            }catch (e:Exception){
                e.printStackTrace()
                Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            }
            else -> tm.deviceId
        }
        return deviceId ?: "" // ?:左边为空才执行右边
    }

    /**
     * 获取应用版本
     * @param context
     * @return
     */
    fun getAppVersionName(context: Context? = mContext): String {
        return try {
            val pm = mContext?.applicationContext?.packageManager
            val packageInfo = pm?.getPackageInfo(context?.packageName, PackageManager.GET_ACTIVITIES)
            packageInfo?.versionName ?: "0"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "0"
        }
    }
    /**
     * 获取应用版本号
     * @param context
     * @return
     */
    fun getAppVersionCode(context: Context? = mContext): Int {
        return try {
            val pm = mContext?.applicationContext?.packageManager
            val packageInfo = pm?.getPackageInfo(context?.packageName, PackageManager.GET_ACTIVITIES)
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                packageInfo?.longVersionCode?.toInt() ?: 0
            }else {
                packageInfo?.versionCode ?: 0
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            0
        }
    }
    /**
     * 获取当前App最大可用内存
     * @return
     */
    fun getMaxMemory(): Int {
        return (Runtime.getRuntime().maxMemory() / 1024).toInt()
    }
    /**
     * 获取当前手机的cpu架构
     * @return
     */
    fun getCpuABI(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Arrays.toString(Build.SUPPORTED_ABIS)
        }else {
            Build.CPU_ABI
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
    fun getOsLevel(): String {
        return "Android " + Build.VERSION.RELEASE
    }

    /**
     * 获取当前手机设备信息,生产厂家+品牌+型号
     * @return
     */
    fun getDeviceInfo(): String {
        return Build.MANUFACTURER + "_" + Build.BRAND + "_" + Build.MODEL
    }

    /**
     * 获取设备mac地址
     * @param context
     * @return
     */
    fun getMacAddress(context: Context): String {
        val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wm.connectionInfo.macAddress
    }

    /**
     * 品牌名
     * @return
     */
    fun getDeviceProduct(): String {
        return Build.PRODUCT
    }
    /**
     *  获取设备屏幕宽度
     *  @param context
     *  @return
     */

    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }
    /**
     *  获取设备屏幕高度
     *  @param context
     *  @return
     */
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }
}