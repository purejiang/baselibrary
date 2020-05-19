package com.nice.baselibrary.base.utils

import android.content.Context
import android.util.Log
import com.nice.baselibrary.base.common.Constant
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.util.*

/**
 * 日志工具类
 * @author JPlus
 * @date 2019/3/14.
 */
object LogUtils {

    /**
     * 是否是debug模式
     */
    private var mDebug = true

    /**
     * log的tag
     */
    private var mTag = ""
    /**
     * 日志最大数量
     */
    private var mMaxNum = 0
    /**
     * 存放日志目录
     */
    private var mDir: File? = null

    /**
     * 初始化
     * @param context 上下文
     * @param debug debug开关，默认开启
     * @param maxNum 最大数量。默认为3
     * @param tag 日志的tag，默认为 <包名> -log
     * @param dir 存放目录，默认为根目录下+包名+log目录
     */
    fun init(context: Context, debug: Boolean = true, maxNum: Int = 3, tag: String = "${context.packageName} -log", dir: File = File(File(Constant.Path.ROOT_DIR, context.packageName), Constant.Path.LOGCAT_INFO_DIR)) {
        mDebug = debug
        mTag = tag
        mDir = dir
        mMaxNum = maxNum
    }

    fun e(message: String, tag: String = mTag) {
        val info = getAutoJumpLogInfo()
        if (mDebug) Log.e(tag, "info:${info[0]} ${info[1]} ${info[2]}, msg:$message")
    }

    fun d(message: String, tag: String = mTag) {
        val info = getAutoJumpLogInfo()
        if (mDebug) Log.d(tag, "info:${info[0]} ${info[1]} ${info[2]}, msg:$message")
    }

    fun w(message: String, tag: String = mTag) {
        val info = getAutoJumpLogInfo()
        if (mDebug) Log.w(tag, "info${info[0]} ${info[1]} ${info[2]}, msg:$message")
    }

    fun v(message: String, tag: String = mTag) {
        val info = getAutoJumpLogInfo()
        if (mDebug) Log.v(tag, "info:${info[0]} ${info[1]} ${info[2]}, msg:$message")
    }

    fun i(message: String, tag: String = mTag) {
        val info = getAutoJumpLogInfo()
        if (mDebug) Log.i(tag, "info:${info[0]} ${info[1]} ${info[2]}, msg:$message")
    }


    fun saveLog(file: File = File(mDir, Date(System.currentTimeMillis()).getDateTimeByMillis(false) + ".log")) {
        //要过滤的类型 *:W表示warm ，我们也可以换成 *:D ：debug， *:I：info，*:E：error等等,*后不加代表全部
        val running = arrayOf("logcat", "-s", "adb logcat *:V")
        //执行命令行
        val exec = ExcCommand.exc(running)
        //协程写文件
        writeNewFile(file, exec)
    }

    /**
     * 获取所有崩溃日志
     * @return 日志文件列表
     */
    fun getAllFiles(): MutableList<File>? {
        return mDir?.getDirFiles()
    }

    /**
     * 获取最新崩溃日志
     * @return 最新文件
     */
    fun getNewFile(): File? {
        //筛选出最近最新的一次崩溃日志
        return getAllFiles()?.let {
            if (it.size > 0) it.reversed()[0] else null
        }
    }

    private fun writeNewFile(file: File, input: InputStream) {
        getAllFiles()?.let {
            if (it.size >= mMaxNum) {
                //大于设置的数量则删除最旧文件
                it.sorted()[0].delete()
            }
            //继续存崩溃日志，协程写入文件
            GlobalScope.launch {
                file.writeFile(input, false)
            }
        }
    }

    /**
     * 获取当前log所在的堆栈信息
     * @return Array<String>{类名， 方法名， 行数}
     */
    private fun getAutoJumpLogInfo(): Array<String> {
        val info = Array(3) {
            ""
        }
        val elements = Thread.currentThread().stackTrace

        return if (elements.size < 5) {
            info
        } else {
            info[0] = elements[5].className.substring(elements[5].className.lastIndexOf(".") + 1)
            info[1] = "${elements[5].methodName}()"
            info[2] = ":${elements[5].lineNumber}"
            info
        }
    }
}