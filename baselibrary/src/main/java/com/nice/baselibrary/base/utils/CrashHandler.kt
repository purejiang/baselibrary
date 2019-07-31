package com.nice.baselibrary.base.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.nice.baselibrary.base.net.OkhttpManager
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Exception
import kotlin.collections.HashMap


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
    private var mSaveFile = false
    private var mDir: File? = null
    /**
     * 初始化
     * @param context 上下文
     * @param url 上传地址
     * @param saveFile 是否保存并上传崩溃日志
     */
    fun init(context: Context, url: String, saveFile: Boolean, callback: Callback<ResponseBody>, dir: File=File(FileUtils.writePrivateDir("crash", context).absolutePath)) {
        mContext = context
        mSaveFile = saveFile
        mDir = dir
        if(mSaveFile) {
            uploadBugFile(url, dir, callback)
        }
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 当系统中有未被捕获的异常，系统将会自动调用 uncaughtException 方法
     * @param thread
     * @param exception
     */
    override fun uncaughtException(thread: Thread?, exception: Throwable?) {
        val name = AppUtils.getInstance().getDeviceImei(mContext!!) + "_" + DateUtils.getDateTimeByMillis(false).replace(":", "-")
        val exceptionInfo = StringBuilder(name + "\n\n" + getSysInfo() + "\n\n" + exception?.message)
        exceptionInfo.append("\n" + getExceptionInfo(exception))
        //新线程写入文件
        if (mSaveFile) {
            Thread(Runnable {
                FileUtils.writeFile(File(mDir, "$name.log"), exceptionInfo.toString(), false)
            }).start()
        }
        // 系统默认处理
        mDefaultCrashHandler?.uncaughtException(thread, exception)
    }

    private fun getSysInfo(): String {
        val map = hashMapOf<String, String>()
        map["versionName"] = AppUtils.getInstance().getAppVersionName(mContext)
        map["versionCode"] = "" + AppUtils.getInstance().getAppVersionCode(mContext)
        map["androidApi"] = "" + AppUtils.getInstance().getOsLevel()
        map["product"] = "" + AppUtils.getInstance().getDeviceProduct()
        map["mobileInfo"] = AppUtils.getInstance().getDeviceInfo()
        map["cpuABI"] = AppUtils.getInstance().getCpuABI()
        val str = StringBuilder("=".repeat(10)+"PhoneInfo"+"=".repeat(10)+"\n")
        for(item in map){
            str.append(item.key).append(" = ").append(item.value).append("\n")
        }
        str.append("=".repeat(10)+"=".repeat(10)+"\n")
        return str.toString()
    }

    private fun getExceptionInfo(exception: Throwable?): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        exception?.printStackTrace(pw)
        return sw.toString()
    }

    private fun uploadBugFile(url:String, dir:File, callback:Callback<ResponseBody>) {
        val body: MultipartBody.Part
        try {
            //筛选出最近的一次崩溃日志
            val file = FileUtils.getDirFiles(dir)!!.reversed()[0]
            val request = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            body = MultipartBody.Part.createFormData("crash", file.name, request)
        } catch (e:Exception) {
            e.printStackTrace()
            return
        }
        OkhttpManager.uploadFile(url, body, callback)
    }
}