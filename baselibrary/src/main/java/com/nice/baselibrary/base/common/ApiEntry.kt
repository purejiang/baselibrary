package com.nice.baselibrary.base.common

import android.content.Context
import com.nice.baselibrary.base.ui.view.NiceShowView
import com.nice.baselibrary.base.utils.AppUtils
import com.nice.baselibrary.base.utils.CrashHandler
import com.nice.baselibrary.base.utils.LogUtils
import com.nice.baselibrary.base.utils.NicePermissions


/**
 * 依赖入口
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

//    /**
//     * 权限初始化
//     * @param context
//     */
//    fun initPermission(context: Context) {
//        //初始化权限管理工具
//        NicePermissions.getInstance().init(context.applicationContext)
//
//    }
//    /**
//     * 入口初始化
//     * @param context
//     * @param debug
//     */
//    fun init(context: Context, logUrL:String, debug: Boolean){
//
//    }
//
//    fun requestPermission(context: Context, permissions: MutableSet<String>?, listener: NicePermissions.PermissionListener) {
//        //请求权限
//        NicePermissions.getInstance().requestPermissions(context, permissions, listener)
//    }
//
//    fun handleRequestPermissionsResult( requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        NicePermissions.getInstance().handleRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
//
//    /**
//     * 销毁
//     */
//    fun destroy() {
//        NicePermissions.getInstance().destroy()
//    }

}