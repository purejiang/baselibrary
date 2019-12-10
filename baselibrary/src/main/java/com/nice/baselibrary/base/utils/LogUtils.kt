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
    private var mTag = "tag"
    private var mMaxNum = 0
    private var mDirPath = Constant.Path.ROOT_DIR

    fun init(context:Context, debug:Boolean){
        val maxNum =1
        val dir = Constant.Path.ROOT_DIR + File.separator + context.packageName + File.separator + Constant.Path.LOGCAT_INFO_DIR
        val tag = " --log: "+context.packageName
        init(debug, maxNum, tag, dir)
    }

    fun init(debug:Boolean, maxNum:Int, tag:String, dir:String){
        mDebug = debug
        mTag = tag
        mDirPath = dir
        mMaxNum = maxNum
        FileUtils.createOrExistsDir(File(mDirPath))
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
     * @param file 默认为根目录下
     */
    fun saveLog(file:File= File(mDirPath, Date(System.currentTimeMillis()).getDateTimeByMillis(false)+".log")){
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
        return FileUtils.getDirFiles(File(mDirPath))
    }

    /**
     * 获取最新崩溃日志
     * @return 最新文件
     */
    fun getNewFile(): File? {
        //筛选出最近最新的一次崩溃日志
        return FileUtils.getDirFiles(File(mDirPath))?.let {
            if (it.size>0) it.reversed()[0] else null
        }
    }

    private fun writeNewFile(file: File, input: InputStream) {
        FileUtils.getDirFiles(File(mDirPath))?.let {
            if (it.size >= mMaxNum) {
                //大于设置的数量则删除最旧文件
                FileUtils.delFileOrDir(it.sorted()[0])
            }
            //继续存崩溃日志，协程写入文件
            GlobalScope.launch{
                FileUtils.writeFile(file, input, false)
            }
        }
    }

}