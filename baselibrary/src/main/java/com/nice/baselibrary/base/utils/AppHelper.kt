package com.nice.baselibrary.base.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.nice.baselibrary.base.common.Constant
import com.nice.baselibrary.base.entity.vo.AppInfo
import java.text.SimpleDateFormat
import java.util.*


/**
 *  App基本方法
 * @author JPlus
 * @date 2019/2/22.
 */

/**
 * 获取当前应用名称
 * @return
 */
fun Context.getAppName(): String {
    return try {
        this.packageManager.let {
            it.getApplicationLabel(it.getApplicationInfo(this.packageName, 0)) as String
        }
    } catch (e: PackageManager.NameNotFoundException) {
        Constant.Persistence.DEFAULT_APP_NAME
    }
}

/**
 * 获取手机已安装的应用
 * @param isSystem 是否包含系统应用
 * @return
 */
fun Context.getAppsInfo(isSystem: Boolean): MutableList<AppInfo> {
    val appsInfo: MutableList<AppInfo> = ArrayList()
    val packageList: List<PackageInfo> = this.packageManager.getInstalledPackages(0)
    packageList.forEach {
        val term: Boolean = if (!isSystem) {
            (it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0
        } else {
            (it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        }

        if (term) {
            val info: ApplicationInfo = this.packageManager.getApplicationInfo(it.packageName, 0)
            val date = Date(it.firstInstallTime)
            val sp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val appInfo = AppInfo(
                    appName = it.applicationInfo.loadLabel(this.packageManager).toString(),
                    packageName = it.packageName,
                    versionCode = it.versionCode,
                    versionName = it.versionName,
                    icon = info.loadIcon(this.packageManager),
                    inTime = sp.format(date),
                    signMd5 = this.getSignerMD5(it.packageName),
                    permission = this.getPermissions(it.packageName)
            )
            appsInfo.add(appInfo)
        }
    }
    return appsInfo
}

/**
 * 获取指定app的签名md5
 */
fun Context.getSignerMD5(packageName: String): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        this.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES).signingInfo.apkContentsSigners[0].toByteArray().encryptionMD5()
    } else {
        this.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures[0].toByteArray().encryptionMD5()
    }
}

/**
 * 获取指定包名的应用的权限列表
 * @param packageName 包名
 * @return
 */
private fun Context.getPermissions(packageName: String): MutableList<String> {
    this.packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions?.let {
        return it.toMutableList()
    }
    return mutableListOf()
}

/**
 * 获取当前包名
 * @return
 */
fun Context.getPackageName(): String {
    return this.packageName
}

/**
 * 获取设备标识
 * @return
 */
fun Context.getDeviceIMEI(): String {
    val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val deviceId = Settings.System.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
    return deviceId ?: "" // ?:左边为空才执行右边
}

/**
 * 获取应用版本
 * @return
 */
fun Context.getAppVersionName(): String {
    return try {
        this.packageManager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        "0"
    }
}

/**
 * 获取应用版本号
 * @return
 */
fun Context.getAppVersionCode(): Int {
    return try {
        this.packageManager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES).let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) it.longVersionCode.toInt() else it.versionCode
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

