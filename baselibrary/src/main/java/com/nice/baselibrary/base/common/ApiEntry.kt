package com.nice.baselibrary.base.common

import android.app.Activity
import android.content.Context
import com.nice.baselibrary.base.ui.view.NiceShowView
import com.nice.baselibrary.base.utils.AppUtils
import com.nice.baselibrary.base.utils.CrashHandler
import com.nice.baselibrary.base.utils.LogUtils
import com.nice.baselibrary.base.utils.PermissionUtils


/**
 * @author JPlus
 * @date 2019/3/15.
 */
class ApiEntry private constructor() {

    companion object {
        private var mApiEntry: ApiEntry? = null
        fun getInstance(): ApiEntry {
            if (mApiEntry == null) {
                synchronized(ApiEntry::class.java) {
                    mApiEntry = ApiEntry()
                }
            }
            return mApiEntry!!
        }
    }

    /**
     * 入口初始化
     * @param context
     * @param debug
     */
    fun init(context: Context, debug: Boolean) {
        //初始化崩溃日志工具
        CrashHandler.getInstance().init(context.applicationContext)

        //初始化日志工具
        LogUtils.getInstance().init(context.applicationContext, debug)

        //初始化AppUtil
        AppUtils.getInstance().init(context.applicationContext)

        //初始化niceShowView
        NiceShowView.getInstance().init(context.applicationContext)

    }

    fun requestPermission(activity: Activity) {
        //初始化权限处理
        PermissionUtils.getInstance().init(activity)
        PermissionUtils.getInstance().requestPermissions()
    }

    fun requestPermission(activity: Activity, permission: String) {
        //初始化权限处理
        PermissionUtils.getInstance().init(activity)
        PermissionUtils.getInstance().requestPermissions(permission)
    }

    fun handleRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        PermissionUtils.getInstance().handleRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * 销毁
     */
    fun destroy() {
        PermissionUtils.getInstance().destroy()
    }

}