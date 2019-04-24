package com.nice.baselibrary.base.utils

import android.annotation.SuppressLint
import android.content.Context
import com.nice.baselibrary.base.common.Constant

import java.io.*

/**
 * 崩溃日志处理类
 * @author JPlus
 * @date 2019/3/14.
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mCrashHandler: CrashHandler? = null

        fun getInstance(): CrashHandler {
            if (mCrashHandler == null) {
                synchronized(CrashHandler::class.java) {
                    mCrashHandler = CrashHandler()
                }
            }
            return mCrashHandler!!
        }
    }

    private var mDefaultCrashHandler: Thread.UncaughtExceptionHandler? = null
    private var mContext: Context? = null

    /**
     * 初始化
     * @param context
     */
    fun init(context: Context) {
        mContext = context
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 当系统中有未被捕获的异常，系统将会自动调用 uncaughtException 方法
     * @param thread
     * @param exception
     */
    override fun uncaughtException(thread: Thread?, exception: Throwable?) {
        val date = StringUtils.getDateTime()
        val filePath = Constant.Path.ROOT_DIR + AppUtils.getInstance().getPackageName(mContext) + Constant.Path.CRASH_INFO_DIR
        val exceptionInfo = StringBuilder(date + "\n" + exception?.message + "\n")
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        exception?.printStackTrace(pw)
        exceptionInfo.append(sw.toString())
        //新线程写入文件
        Thread(Runnable {
            FileUtils.writeFile(File(filePath, date + ".log"), ByteArrayInputStream(exceptionInfo.toString().toByteArray()), false)
        }).start()
        //返回给系统处理异常
        mDefaultCrashHandler?.uncaughtException(thread, exception)
    }
}