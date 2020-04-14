package com.nice.baselibrary.base.utils

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.nice.baselibrary.base.common.Constant
import com.nice.baselibrary.base.vo.AppInfo
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
 * 获取手机已安装的非系统应用
 * @param isSystem
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

/**
 * 获取当前手机的cpu架构
 * @return
 */
fun getCpuABI(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) Arrays.toString(Build.SUPPORTED_ABIS) else Build.CPU_ABI
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
    return Build.VERSION.RELEASE
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
 * @return
 */
@SuppressLint("MissingPermission")
fun Context.getMacAddress(): String {
    return (this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).connectionInfo.macAddress
}

/**
 * 品牌名
 * @return
 */
fun getDeviceProduct(): String {
    return Build.PRODUCT
}

/**
 *  获取可用显示尺寸的绝对宽度（以像素为单位）
 *  @return
 */

fun Context.getScreenWidth(): Int {
    return this.resources.displayMetrics.widthPixels
}

/**
 *  获取可用显示尺寸的绝对高度（以像素为单位）
 *  @return
 */
fun Context.getScreenHeight(): Int {
    return this.resources.displayMetrics.heightPixels
}

/**
 *  根据包名跳转到第三方应用，不重复启动
 *  @param packageName
 *  @return
 */
fun Context.startAppByPackageName(packageName: String) {
    Intent(Intent.ACTION_MAIN).let {
        var mainAct = ""
        it.addCategory(Intent.CATEGORY_LAUNCHER)
        it.flags = Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK
        val list = this.packageManager.queryIntentActivities(it, PackageManager.MATCH_DEFAULT_ONLY)
        for (i in list.indices) {
            val info = list[i]
            if (info.activityInfo.packageName == packageName) {
                mainAct = info.activityInfo.name
                break
            }
        }
        if (mainAct.isEmpty()) return

        it.component = ComponentName(packageName, mainAct)
        this.startActivity(it)
    }
}

/**
 * 安装apk
 * @param path apk文件路径
 * @param authority fileProvider的authority
 */
fun Context.installApk(path: String, authority: String) {
    Intent(Intent.ACTION_VIEW).let {
        it.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.setDataAndType(
                this.path2Uri(path, authority),
                "application/vnd.android.package-archive"
        )
        this.startActivity(it);
    }
}

/**
 *  卸载第三方应用
 *  @return
 */
fun Context.deleteAppByPackageName(packageName: String) {
    Intent(Intent.ACTION_DELETE).let {
        it.data = Uri.parse("package:$packageName")
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(it)
    }
}