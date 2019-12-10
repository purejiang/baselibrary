package com.nice.baselibrary.base.utils

import android.content.Context
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*


/**
 * 崩溃日志处理类
 * @author JPlus
 * @date 2019/3/14.
 */

object CrashHandler : Thread.UncaughtExceptionHandler {
    private var mDefaultCrashHandler: Thread.UncaughtExceptionHandler? = null
    private var mDirPath: String? = null
    private var mMaxNum = 0
    private var mFileName = "crash"
    private var mSysInfo = "暂无信息"
    /**
     * 初始化
     * @param maxNum 最大保存文件数量，默认为1
     * @param dir 存储文件的目录，默认为应用私有文件夹下crash目录
     */
    fun init(context: Context, maxNum: Int = 1, dir: String = FileUtils.writePrivateDir("crash", context).absolutePath) {
        mDirPath = dir
        mMaxNum = maxNum
        mFileName = context.getDeviceImei() + "_" + Date(System.currentTimeMillis()).getDateTimeByMillis(false).replace(":", "-")
        mSysInfo = getSysInfo(context)
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 获取所有崩溃日志
     * @return 日志文件列表
     */
    fun getAllFiles(): MutableList<File>? {
        return FileUtils.getDirFiles(File(mDirPath))
    }

    /**
     * 获取最新崩溃日志
     * @return 最新文件
     */
    fun getNewFile(): File? {
        //筛选出最近最新的一次崩溃日志
        return FileUtils.getDirFiles(File(mDirPath))?.let {
            if (it.size > 0) it.reversed()[0] else null
        }
    }

    private fun writeNewFile(path: String, name: String, body: String) {
        FileUtils.getDirFiles(File(mDirPath))?.let {
            if (it.size >= mMaxNum) {
                //大于设置的数量则删除最旧文件
                FileUtils.delFileOrDir(it.sorted()[0])
            }
            //继续存崩溃日志，新线程写入文件
            GlobalScope.launch {
                FileUtils.writeFile(File(path, name), body, false)
            }
        }
    }

    /**
     * 当系统中有未被捕获的异常，系统将会自动调用 uncaughtException 方法
     * @param thread
     * @param exception
     */
    override fun uncaughtException(thread: Thread?, exception: Throwable?) {
        val exceptionInfo = StringBuilder(mFileName + "\n\n" + mSysInfo + "\n\n" + exception?.message)
        exceptionInfo.append("\n" + getExceptionInfo(exception))
        mDirPath?.let {
            if (mMaxNum > 0) {
                writeNewFile(it, "$mFileName.log", exceptionInfo.toString())
            }
        }
        // 系统默认处理
        mDefaultCrashHandler?.uncaughtException(thread, exception)
    }

    private fun getSysInfo(context: Context): String {
        val map = hashMapOf<String, String>()
        map["versionName"] = context.getAppVersionName()
        map["versionCode"] = "" + context.getAppVersionCode()
        map["androidApi"] = "" + getOsLevel()
        map["product"] = "" + getDeviceProduct()
        map["mobileInfo"] = getDeviceInfo()
        map["cpuABI"] = getCpuABI()
        val str = StringBuilder("=".repeat(10) + "PhoneInfo" + "=".repeat(10) + "\n")
        for (item in map) {
            str.append(item.key).append(" = ").append(item.value).append("\n")
        }
        str.append("=".repeat(10) + "=".repeat(10) + "\n")
        return str.toString()
    }

    private fun getExceptionInfo(exception: Throwable?): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        exception?.printStackTrace(pw)
        return sw.toString()
    }

}