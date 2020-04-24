package com.nice.baselibrary.base.common

import android.app.Activity
import android.content.Context
import android.os.Environment
import com.nice.baselibrary.base.utils.*
import java.io.File
import java.util.*


/**
 * 依赖入口
 * @author JPlus
 * @date 2019/3/15.
 */
class BaseLibrary private constructor() {
    companion object {
        val instance: BaseLibrary by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            BaseLibrary()
        }
    }

    /**
     * 初始化，最好写在application中
     * @param context 上下文
     * @param isDebug 是否debug模式
     * @param dexDir 热修复dex存放目录,为空时不加载dex
     */
    fun initUtils(context: Context, isDebug: Boolean, dexDir: File? = null) {
        context.applicationContext.let {
            //初始化权限管理工具
            JPermissionsUtils.init(it)

            //加载dex,用于热修复
            dexDir?.let { dir ->
                PatchDexUtils.loadDex(it, dir)
            }
            //初始化日志工具
            LogUtils.init(it, isDebug)

            //崩溃日志工具
            CrashHandler.init(it, listener = object :CrashHandler.CrashListener{
                override fun backFileRule(): String {
                    return "crash_" + Date(System.currentTimeMillis()).getDateTimeByMillis(false).replace(":", "-") + ".text"
                }
            })
        }
    }

    /**
     * 请求权限
     */
    fun requestPermissions(activity: Activity, permissions: MutableSet<String>?, listener: JPermissionsUtils.PermissionListener) {
        //请求权限
        JPermissionsUtils.requestPermissions(activity, permissions, listener)
    }

    /**
     * 请求权限结果
     */
    fun handleRequestPermissionsResult(activity: Activity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        JPermissionsUtils.handleRequestPermissionsResult(activity, requestCode, permissions, grantResults)
    }

    /**
     * 销毁
     */
    fun destroy() {

    }
}