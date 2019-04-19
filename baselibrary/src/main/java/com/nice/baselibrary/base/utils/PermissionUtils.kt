package com.nice.baselibrary.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nice.baselibrary.base.ui.view.NiceDialog
import com.nice.baselibrary.base.ui.view.NiceShowView
import java.io.IOException
import java.lang.RuntimeException


/**
 * 动态权限工具类
 * @author JPlus
 * @date 2019/2/27.
 */
class PermissionUtils private constructor() {

    companion object {
        private val REQUEST_CODE_ASK_PERMISSIONS = 1024
        private var mPermissionUtils: PermissionUtils? = null
        fun getInstance(): PermissionUtils {
            if (mPermissionUtils == null) {
                synchronized(PermissionUtils::class.java) {
                    mPermissionUtils = PermissionUtils()
                }
            }
            return mPermissionUtils!!
        }

    }

    private var mContext: Context? = null
    private var mNiceDialog: NiceDialog? = null
    private var mOpenWindow: Boolean = false
    private var mIsCancelable: Boolean = false
    private var mPerMap: HashMap<String, HashMap<String,List<String>>> ?= null
    fun init(context: Context, openWindow: Boolean, isCancelable: Boolean) {
        mContext = context
        mOpenWindow = openWindow
        mIsCancelable = isCancelable
        //获取json文件中的权限信息
        try {
            val input = context.resources.assets.open("permissions.json")
            val perContent = FileUtils.readFile2String(input, "UTF-8")
            mPerMap = Gson().fromJson<HashMap<String,HashMap<String,List<String>>>>(perContent, object : TypeToken<HashMap<String, HashMap<String, List<String>>>>() {}.type)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun init(context: Context, openWindow: Boolean) {
        init(context, openWindow, true)
    }

    fun init(context: Context) {
        init(context, true)
    }

    /**
     * 获取应用注册的权限
     * @param context 上下文
     * @return 应用在manifest中注册的权限数组
     */
    private fun getAllPermissions(context: Context): Array<String>? {
        return try {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)?.requestedPermissions //此列表包括所有请求的权限，甚至包括系统在安装时未授予或已知的权限
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            throw RuntimeException(e.message, e)
        }

    }

    /**
     * 检查是否全部通过权限
     * @param params
     * @return 未通过的权限列表
     */
    private fun getNoPermission(params: Array<String>?): Array<String> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || params == null || params.isEmpty()) {
            return arrayOf()
        }
        val sb = StringBuilder("getNoPermission").append("\n")
        val noPermission: MutableList<String> = ArrayList()
        params.filter { ContextCompat.checkSelfPermission(mContext!!, it) != PackageManager.PERMISSION_GRANTED }//筛选未通过的权限
                .forEach {
                    noPermission.add(it)
                    sb.append(it).append("\n")
                }
        LogUtils.getInstance().d(sb.toString())
        return noPermission.toTypedArray()
    }

    /**
     * 是否有忽略的权限
     * @param params
     * @return 被忽略的权限列表
     */
    private fun shouldShowRequestPermissions(params: Array<String>): Array<String> {
        val sb = StringBuilder("shouldShowRequestPermissions").append("\n")
        if (params.isEmpty()) {
            return arrayOf()
        }
        val ignorePermissions: MutableList<String> = ArrayList()
        params.filter { !ActivityCompat.shouldShowRequestPermissionRationale(mContext as Activity, it)&& isDangerPermission(it) }//筛选被用户忽略的权限
                .forEach {
                    ignorePermissions.add(it)
                    sb.append(it).append("\n")
                }
        LogUtils.getInstance().d(sb.toString())
        return ignorePermissions.toTypedArray()//返回数组
    }

    /**
     * 判断是否危险权限
     * @param permission
     */
    fun isDangerPermission(permission:String):Boolean{
        val dangerPermission = mPerMap!!["danger_permissions"]
        for (perInfoList:List<String> in dangerPermission?.values!!){
            perInfoList
                    .filter { it.contains(permission) }
                    .forEach {
                        LogUtils.getInstance().e(it)
                        return true }
        }
        return false
    }
    /**
     * 跳转到应用的设置界面
     * @param activity
     */
    private fun startActivityToSetting(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + AppUtils.getInstance().getPackageName())
        activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    /**
     * 跳转到应用的弹出框设置界面
     * @param activity
     */
    private fun startActivityToOverlay(activity: Activity) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.parse("package:" + AppUtils.getInstance().getPackageName())
        activity.startActivityForResult(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), REQUEST_CODE_ASK_PERMISSIONS)
    }


    /**
     * 请求所有权限
     */
    fun requestPermissions() {
        requestPermissions(mContext!!)
    }

    /**
     * 请求所有权限
     * @param context
     */
    fun requestPermissions(context: Context) {
        LogUtils.getInstance().e("requestPermissions", "manyfunction")
        val mNoPermission = getNoPermission(getAllPermissions(context))
        if (mNoPermission.isNotEmpty()) {
            ActivityCompat.requestPermissions(context as Activity, mNoPermission, REQUEST_CODE_ASK_PERMISSIONS)
        }
    }

    /**
     * 请求单个权限后能够获取整组权限
     *
     * @param permission 需要请求的权限
     */
    fun requestPermissions(permission: String) {
        requestPermissions(permission, mContext!!)
    }

    /**
     * 请求单个权限后能够获取整组权限
     *
     * @param permission 需要请求的权限
     * @param context 上下文
     */
    fun requestPermissions(permission: String?, context: Context) {
        if (permission == null) {
            return
        }
        val permissions = arrayOf(permission)
        val noPermissions = getNoPermission(permissions)
        if (permissions.isNotEmpty() && noPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(context as Activity, permissions, REQUEST_CODE_ASK_PERMISSIONS)
        }
    }

    /**
     * 弹出权限设置提醒框
     * @param context
     * @param permissions
     * @return
     */
    private fun showPermissionDialog(context: Context, permissions: Array<String>): NiceDialog {
        LogUtils.getInstance().e("showPermissionDialog", "manyfunction")
        val sb = StringBuilder("您有已忽略的权限，请到设置中开启:\n\n")

        //获取危险权限信息
        for (per in permissions) {
            val dangerPermission = mPerMap!!["danger_permissions"]
            for (perInfoList:List<String> in dangerPermission?.values!!){
                perInfoList
                        .filter { it.contains(per) }
                        .forEach { sb.append("\t\t\t\t").append(it.split(" ")[1]).append("\n") }
            }
        }
        return NiceShowView.getInstance().createDialog(context, NiceDialog.DIALOG_NORMAL)
                .setTitle("关于权限")
                .setMessage(sb.toString())
                .setCanceled(mIsCancelable)
                .setCancel("取消", object : NiceDialog.DialogClickListener {
                    override fun onClick() {
                        destroy()
                    }
                })
                .setConfirm("去设置", object : NiceDialog.DialogClickListener {
                    override fun onClick() {
                        startActivityToSetting(context as Activity)
                    }
                })
    }

    /**
     * 避免内存泄漏
     */
    fun destroy() {
        if (mContext != null) {
            mContext = null
        }
        if (mNiceDialog != null) {
            mNiceDialog?.dismiss()
            mNiceDialog = null
        }
    }

    /**
     * 是否请求到权限
     * @param requestCode
     * @param permissions
     * @param grantResult
     */
    fun handleRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResult: IntArray) {
        handleRequestPermissionsResult(mContext!!, requestCode, permissions, grantResult)
    }

    /**
     * 是否请求到权限
     * @param context 上下文
     * @param requestCode 请求码
     * @param permissions 请求的权限数组
     * @param grantResult 请求的结果码
     */
    fun handleRequestPermissionsResult(context: Context, requestCode: Int, permissions: Array<out String>, grantResult: IntArray) {
        var result = true
        val noPermissionList:MutableList<String> = ArrayList()
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> {
                for (i in grantResult.indices) {
                    result = result && grantResult[i] == PackageManager.PERMISSION_GRANTED
                    if (grantResult[i] != PackageManager.PERMISSION_GRANTED) {
                        //未通过的权限
                        noPermissionList.add(permissions[i])
                    }
                }
                //是否请求到全部权限的
                if (result) {
                    LogUtils.getInstance().d("success", "tag")
                } else {
                    LogUtils.getInstance().d("failed", "tag")
                }
            }
            else -> {
            }
        }

        val noPermissions = noPermissionList.toTypedArray()
        val ignorePermissions = shouldShowRequestPermissions(noPermissions)
        if (ignorePermissions.isNotEmpty()) {
            if (mOpenWindow) {
                if (mNiceDialog == null) {
                    mNiceDialog = showPermissionDialog(context, ignorePermissions)
                }
                mNiceDialog?.show()
            }
        }
        val sb = StringBuilder("handleRequestPermissionsResult").append("\n")
        for (per in noPermissions) {
            sb.append(per).append("\n")
        }
        sb.append("" + requestCode + "\n")
        for (grant in grantResult) {
            sb.append("" + grant).append("\n")
        }
        LogUtils.getInstance().d(sb.toString(), "manyfunction")
    }

    /**
     * 获取app的录音权限是否打开
     * 兼容Android 6.0 以下版本
     * @param context
     */
    fun hasAudioPermission(context: Context): Boolean {
        // 音频获取源
        val audioSource = MediaRecorder.AudioSource.MIC
        // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
        val sampleRateInHz = 44100
        // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
        val channelConfig = AudioFormat.CHANNEL_IN_STEREO
        // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        // 缓冲区字节大小
        val bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, audioFormat)
        var audioRecord: AudioRecord? = AudioRecord(audioSource, sampleRateInHz,
                channelConfig, audioFormat, bufferSizeInBytes)
        try {
            // 防止某些手机崩溃
            audioRecord!!.startRecording()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

        /**
         * 根据开始录音判断是否有录音权限
         */
        if (audioRecord!!.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
            return false
        }
        audioRecord.stop()
        audioRecord.release()
        audioRecord = null
        return true
    }
}



