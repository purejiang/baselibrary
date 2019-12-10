package com.nice.baselibrary.base.common

import android.app.Activity
import android.content.Context
import com.nice.baselibrary.base.utils.*
import com.nice.baselibrary.widget.NiceShowView
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

    private var mContext: Context? = null

    /**
     * 初始化，最好写在application中
     *
     */
    fun init(context: Context) {
        mContext = context.applicationContext
        //初始化AppUtil
        AppUtils.instance.init(context)
    }

    fun initUtils(isDebug:Boolean){
        mContext?.let{
            //初始化权限管理工具
            JPermissionsUtils.instance.init(it.applicationContext)

            //初始化NiceShowView
            NiceShowView.instance.init(it)

            //加载dex,用于热修复
            PatchDexUtils.loadDex(it.applicationContext, File(Constant.Path.ROOT_DIR, Constant.Path.PATCH_DEX_DIR))

            //初始化日志工具
            LogUtils.instance.init(it, isDebug)

            //崩溃日志工具
            CrashHandler.instance.init(it)
        }
    }

    /**
     * 请求权限
     */
    fun requestPermissions(activity: Activity, permissions: MutableSet<String>?, listener: JPermissionsUtils.PermissionListener) {
        //请求权限
        JPermissionsUtils.instance.requestPermissions(activity, permissions, listener)
    }

    /**
     * 请求权限结果
     */
    fun handleRequestPermissionsResult(context: Context, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        JPermissionsUtils.instance.handleRequestPermissionsResult(context, requestCode, permissions, grantResults)
    }

    /**
     * 销毁
     */
    fun destroy() {

    }
}