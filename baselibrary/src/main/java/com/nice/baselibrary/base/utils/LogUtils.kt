package com.nice.baselibrary.base.utils

import android.content.Context
import android.util.Log
import com.nice.baselibrary.base.common.Constant
import java.io.File

/**
 * 日志工具类
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
         fun getInstance():LogUtils{
            if(mLogUtils ==null){
                synchronized(LogUtils::class.java) {
                    mLogUtils = LogUtils()
                }
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
    /**
     * 上传log文件地址
     */
    private var mLogUrl = ""

    fun init(context:Context, logUrl:String, debug:Boolean){
        init(context, logUrl, debug, " --log: "+AppUtils.getInstance().getPackageName(context))
    }

    fun init(context:Context, logUrl:String, debug:Boolean, tag:String){
        mDebug = debug
        mContext = context
        mTag = tag
        mLogUrl = logUrl
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
    fun saveLog(file:File){
        //要过滤的类型 *:W表示warm ，我们也可以换成 *:D ：debug， *:I：info，*:E：error等等,*后不加代表全部
        val running = arrayOf("logcat", "-s", "adb logcat *")
        //执行命令行
        val exec = ExcCommand.exc(running)
        //子线程写文件
        if(mDebug) {
            Thread(Runnable {
                FileUtils.writeFile(file, exec, false)
            })
        }
    }
    /**
     * 保存log
     */
    fun saveLog(){
        val filePath = Constant.Path.ROOT_DIR + File.separator + AppUtils.getInstance().getPackageName(mContext!!) + File.separator + Constant.Path.LOGCAT_INFO_DIR
        val file = File(filePath, DateUtils.getDateTimeByMillis(false)+".log")
        saveLog(file)
    }
}