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
import com.nice.baselibrary.base.view.NiceDialog
import com.nice.baselibrary.base.view.NiceShowView
import java.io.IOException
import java.lang.RuntimeException


/**
 * 动态权限工具类
 * @author JPlus
 * @date 2019/2/27.
 */
class PermissionUtils private constructor() {
    /*
     * 九组危险权限
    group:android.permission-group.CALENDAR 日历
        permission:android.permission.READ_CALENDAR 读取日历
        permission:android.permission.WRITE_CALENDAR 写入日历

    group:android.permission-group.CAMERA 照相机
        permission:android.permission.CAMERA

    group:android.permission-group.CONTACTS 通讯录方面
        permission:android.permission.WRITE_CONTACTS 写入通讯录
        permission:android.permission.GET_ACCOUNTS 访问通讯录权限
        permission:android.permission.READ_CONTACTS 读取通讯录

    group:android.permission-group.LOCATION 位置
        permission:android.permission.ACCESS_FINE_LOCATION 获取位置
        permission:android.permission.ACCESS_COARSE_LOCATION 获取粗略定位

    group:android.permission-group.MICROPHONE 扩音器；麦克风
        permission:android.permission.RECORD_AUDIO 录音

    group:android.permission-group.PHONE 电话方面
        permission:android.permission.READ_CALL_LOG 看电话记录
        permission:android.permission.READ_PHONE_STATE 读取手机状态
        permission:android.permission.CALL_PHONE 打电话
        permission:android.permission.WRITE_CALL_LOG 编写调用日志
        permission:android.permission.USE_SIP 使用SIP
        permission:android.permission.PROCESS_OUTGOING_CALLS 过程输出调用
        permission:com.android.voicemail.permission.ADD_VOICEMAIL 添加语音信箱

    group:android.permission-group.SENSORS 传感器
        permission:android.permission.BODY_SENSORS 体传感器
　
    group:android.permission-group.SMS 信息
        permission:android.permission.READ_SMS 读取信息 　　
        permission:android.permission.RECEIVE_WAP_PUSH 收到WAP推送
        permission:android.permission.RECEIVE_MMS 接收彩信
        permission:android.permission.RECEIVE_SMS 收信息
        permission:android.permission.SEND_SMS 发信息
        permission:android.permission.READ_CELL_BROADCASTS 读广播

    group:android.permission-group.STORAGE 存储
        permission:android.permission.READ_EXTERNAL_STORAGE 读取外部存储器  　
        permission:android.permission.WRITE_EXTERNAL_STORAGE 写外部存储器
    */
    companion object {
        private val REQUEST_CODE_ASK_PERMISSIONS = 1024
        private var mPermissionUtils: PermissionUtils? = null
        @Synchronized
        fun getInstance(): PermissionUtils {
            if (mPermissionUtils == null) {
                mPermissionUtils = PermissionUtils()
            }
            return mPermissionUtils!!
        }

    }

    private var mContext: Context? = null
    private var mNiceDialog: NiceDialog? = null
    private var mOpenWindow: Boolean = false
    private var mIsCancelable: Boolean = false

    fun init(context: Context, openWindow: Boolean, isCancelable: Boolean) {
        mContext = context
        mOpenWindow = openWindow
        mIsCancelable = isCancelable
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
        params.filter { !ActivityCompat.shouldShowRequestPermissionRationale(mContext as Activity, it) }//筛选被用户忽略的权限
                .forEach {
                    ignorePermissions.add(it)
                    sb.append(it).append("\n")
                }
        LogUtils.getInstance().d(sb.toString())
        return ignorePermissions.toTypedArray()//返回数组
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
     *
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
     * @return
     */
    private fun showPermissionDialog(context: Context, permissions: Array<String>): NiceDialog {
        LogUtils.getInstance().e("showPermissionDialog", "manyfunction")

        val sb = StringBuilder("您有已忽略的权限，请到设置中开启:\n")
        var pertxt = ArrayList<String>()
        try {
            val input = context.resources.assets.open("permission.txt")
            pertxt = FileUtils.readFile2List(input, "UTF-8")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        for (per in permissions) {
            pertxt.filter { it.contains(per) }
                    .forEach { per2 -> sb.append("\t\t").append(per2.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]).append("\n") }
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


