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
fun Context.getApplicationName(): String {
    val packageManager: PackageManager
    val applicationInfo: ApplicationInfo
    try {
        packageManager = this.applicationContext.packageManager
        applicationInfo = packageManager.getApplicationInfo(this.packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        return Constant.Persistence.DEFAULT_APP_NAME
    }
    return packageManager.getApplicationLabel(applicationInfo) as String
}

/**
 * 获取手机已安装的非系统应用
 * @param isSystem
 * @return
 */
fun Context.getAppsInfo(isSystem: Boolean): MutableList<AppInfo> {
    val appsInfo: MutableList<AppInfo> = ArrayList()
    val packageList: List<PackageInfo> = this.packageManager.getInstalledPackages(0)
    for (packageInfo in packageList) {
        val term: Boolean = if (!isSystem) {
            (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0
        } else {
            (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        }

        if (term) {
            val info: ApplicationInfo = this.packageManager.getApplicationInfo(packageInfo.packageName, 0)
            val date = Date(packageInfo.firstInstallTime)
            val sp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val appInfo = AppInfo(
                    appName = packageInfo.applicationInfo.loadLabel(this.packageManager).toString(),
                    packageName = packageInfo.packageName,
                    versionCode = packageInfo.versionCode,
                    versionName = packageInfo.versionName,
                    icon = info.loadIcon(this.packageManager),
                    inTime = sp.format(date),
                    signMd5 = (this.packageManager.getPackageInfo(
                            packageInfo.packageName, PackageManager.GET_SIGNATURES).signatures[0].toByteArray()).encryptionMD5(),
                    permission = getPermissions(packageInfo.packageName, this.packageManager)
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
private fun Context.getPermissions(packageName: String, packageManager: PackageManager): MutableList<String> {
    val pi: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
    var permissions: MutableList<String> = mutableListOf("此应用无权限信息")
    pi.requestedPermissions?.let {
        permissions = it.toMutableList()
    }
    return permissions
}

/**
 * 获取当前包名
 * @return
 */
fun Context.getPackageName(): String {
    return this.applicationInfo?.packageName ?: ""
}


/**
 * 获取设备标识
 * @return
 */
@SuppressLint("MissingPermission")
fun Context.getDeviceImei(): String {
    val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val deviceId = when {
        Build.VERSION.SDK_INT > Build.VERSION_CODES.P -> Settings.System.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        Build.VERSION.SDK_INT > Build.VERSION_CODES.O -> try {
            tm.imei
        } catch (e: Exception) {
            e.printStackTrace()
            Settings.System.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        }
        else -> tm.deviceId
    }
    return deviceId ?: "" // ?:左边为空才执行右边
}

/**
 * 获取应用版本
 * @return
 */
fun Context.getAppVersionName(): String {
    return try {
        val pm = this.applicationContext?.packageManager
        val packageInfo = pm?.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
        packageInfo?.versionName ?: "0"
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
        val pm = this.applicationContext?.packageManager
        val packageInfo = pm?.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo?.longVersionCode?.toInt() ?: 0
        } else {
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
    } else {
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
    val wm = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
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
 *  @return
 */

fun Context.getScreenWidth(): Int {
    return this.resources.displayMetrics.widthPixels
}

/**
 *  获取设备屏幕高度
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
@SuppressLint("WrongConstant")
fun Context.startAppByPackageName(packageName: String){
    var mainAct =""
    val pkgMag = this.packageManager
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.flags = Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK
    val list = pkgMag.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES)
    for (i in list.indices) {
        val info = list[i]
        if (info.activityInfo.packageName == packageName) {
            mainAct = info.activityInfo.name
            break
        }
    }
    if (mainAct.isEmpty()) {
        return
    }
    intent.component = ComponentName(packageName, mainAct)
    this.startActivity(intent)
}
/**
 *  卸载第三方应用
 *  @return
 */
fun Context.deleteAppByPackageName(packageName: String){
    Intent(Intent.ACTION_DELETE).let{
        it.data = Uri.parse("package:$packageName")
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(it)
    }
}