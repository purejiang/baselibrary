package com.nice.baselibrary.base.common

import android.content.Context
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
        private var mApiEntry: ApiEntry?=null
        @Synchronized fun getInstance(): ApiEntry {
            if(mApiEntry ==null){
                mApiEntry = ApiEntry()
            }
            return mApiEntry!!
        }
    }
    /**
     * 入口初始化
     * @param context
     */
   fun init(context: Context, debug:Boolean){
        //初始化崩溃日志工具
        CrashHandler.getInstance().init(context.applicationContext)
        //初始化权限处理
        PermissionUtils.getInstance().init(context)
        //初始化日志工具
        LogUtils.getInstance().init(context.applicationContext, debug)
        //初始化AppUtil
        AppUtils.getInstance().init(context.applicationContext)
    }

    fun requestPermission(){
        PermissionUtils.getInstance().requestPermissions()

    }
    fun requestPermission(permission:String){
        PermissionUtils.getInstance().requestPermissions(permission)
    }

    fun handleRequestPermissionsResult(requestCode:Int, permissions:Array<out String>, grantResults:IntArray){
        PermissionUtils.getInstance().handleRequestPermissionsResult(requestCode, permissions,grantResults)
    }
    /**
     * 销毁
     */
    fun destory(){

    }

}