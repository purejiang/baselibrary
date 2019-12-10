package com.nice.baselibrary.base.common

import android.app.Activity
import android.content.Context
import com.nice.baselibrary.base.utils.CrashHandler
import com.nice.baselibrary.base.utils.JPermissionsUtils
import com.nice.baselibrary.base.utils.LogUtils
import com.nice.baselibrary.base.utils.PatchDexUtils
import java.io.File


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
     *
     */
    fun initUtils(context: Context, isDebug:Boolean){
        context.applicationContext.let{
            //初始化权限管理工具
            JPermissionsUtils.init(it)

            //加载dex,用于热修复
            PatchDexUtils.loadDex(it, File(Constant.Path.ROOT_DIR, Constant.Path.PATCH_DEX_DIR))

            //初始化日志工具
            LogUtils.init(it, isDebug)

            //崩溃日志工具
            CrashHandler.init(it)
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