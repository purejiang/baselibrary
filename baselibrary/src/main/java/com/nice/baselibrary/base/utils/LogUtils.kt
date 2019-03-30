package com.nice.baselibrary.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.nice.baselibrary.base.common.Constant
import java.io.File

/**
 * @author JPlus
 * @date 2019/3/14.
 */
class LogUtils private constructor() {
    companion object {
        private var mLogUtils:LogUtils?=null
        /**
         * 通过单例获取
         * @return
         */
        @Synchronized fun getInstance():LogUtils{
            if(mLogUtils ==null){
                mLogUtils = LogUtils()
            }
            return mLogUtils!!
        }
    }

    /**
     * 是否是debug模式
     */
    private var mDebug = true
    /**
     * 上下文
     */
    private var mContext:Context?=null
    /**
     * log的tag
     */
    private var mTag = "tag"

    fun init(context:Context, debug:Boolean){
        mDebug = debug
        mContext = context
        mTag = AppUtils.getInstance().getPackageName(context)
    }

    fun e(message:String, tag:String=mTag){
        if(mDebug) {
            Log.e(tag, message)
        }
    }
    fun d(message:String, tag:String=mTag){
        if(mDebug) {
            Log.d(tag, message)
        }
    }
    fun w(message:String, tag:String=mTag){
        if(mDebug) {
            Log.w(tag, message)
        }
    }
    fun v(message:String, tag:String=mTag){
        if(mDebug) {
            Log.v(tag, message)
        }
    }
    fun i(message:String, tag:String=mTag){
        if(mDebug) {
            Log.i(tag, message)
        }
    }
    /**
     * 保存log到文件
     */
    fun saveLog(){
        //要过滤的类型 *:W表示warm ，我们也可以换成 *:D ：debug， *:I：info，*:E：error等等,*后不加代表全部
        val running = arrayOf("logcat", "-s", "adb logcat *")
        val exec = Runtime.getRuntime().exec(running)
        val filePath = Constant.Companion.Path.ROOT_DIR + AppUtils.getInstance().getPackageName(mContext!!) + Constant.Companion.Path.LOGCAT_INFO_DIR
        val file = File(filePath, StringUtils.getDateTime()+".log")
        Thread(Runnable {
            FileUtils.writeFile(file, exec.inputStream, false)
        }).start()

    }
}