package com.nice.baselibrary.base.utils

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nice.baselibrary.widget.NiceShowView
import com.nice.baselibrary.widget.dialog.NiceDialog
import java.io.IOException


/**
 * 动态权限工具类
 * @author JPlus
 * @date 2019/2/27.
 */
class JPermissionsUtils private constructor() {
    companion object {
        private const val REQUEST_CODE_ASK_PERMISSIONS = 1024
        val instance: JPermissionsUtils by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            JPermissionsUtils()
        }
    }

    private var mOpenDialog: Boolean = true
    private var mIsCancelable: Boolean = true
    private var mHasWindow: Boolean = true
    private var mPerMap: MutableMap<String, HashMap<String, List<String>>>? = null
    private var mPerListener: PermissionListener? = null
    private val mAllPermissions: Array<String> by lazy {
        arrayOf<String>()
    }
    private var mPermissions: Array<String>? = null

    private val mPer2Listener: MutableMap<MutableSet<String>, PermissionListener> by lazy {
        HashMap<MutableSet<String>, PermissionListener>()
    }

    fun init(context: Context, openDialog: Boolean = true, isCancelable: Boolean = true, hasWindow: Boolean = true) {
        mPerMap = getDangerPermission(context)
        mOpenDialog = openDialog
        mIsCancelable = isCancelable
        mHasWindow = hasWindow
    }

    /**
     * 获取应用注册的权限
     * @param context 上下文
     * @return 应用在manifest中注册的权限数组
     */
    private fun getAllPermissions(context: Context): Array<String> {
        return try {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)?.requestedPermissions
                    ?: arrayOf() //此列表包括所有请求的权限，甚至包括系统在安装时未授予或已知的权限
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
    private fun getNoPermission(context: Context, params: MutableSet<String>?): MutableSet<String> {
        val noPermission = mutableSetOf<String>()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || params == null || params.isEmpty()) {
            return noPermission
        }
        val sb = StringBuilder("getNoPermission").append("\n")
        params.filter { ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED && isDangerPermission(it) }//筛选未通过的权限
                .forEach {
                    noPermission.add(it)
                    sb.append(it).append("\n")
                }
        LogUtils.instance.d(sb.toString())
        return noPermission
    }

    /**
     * 是否有忽略的权限
     * @param params
     * @return 被忽略的权限列表
     */
    private fun checkIgnorePermissions(context: Context, params: MutableSet<String>): MutableSet<String> {
        val ignorePermissions = mutableSetOf<String>()
        val sb = StringBuilder("checkIgnorePermissions").append("\n")
        if (params.isEmpty()) {
            return ignorePermissions
        }
        params.filter { !ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, it) && isDangerPermission(it) }//筛选被用户忽略的权限
                .forEach {
                    ignorePermissions.add(it)
                    sb.append(it).append("\n")
                }
        LogUtils.instance.d(sb.toString())
        return ignorePermissions
    }

    /**
     * 判断是否危险权限
     * @param permission
     */
    private fun isDangerPermission(permission: String): Boolean {
        val dangerPermission = mPerMap!!["danger_permissions"]
        for (perInfoList: List<String> in dangerPermission?.values!!) {
            perInfoList
                    .filter { it.contains(permission) }
                    .forEach {
                        LogUtils.instance.e(it)
                        return true
                    }
        }
        return false
    }

    /**
     * 检查是否有悬浮窗权限
     *
     * @param context 上下文
     * @return 是否有悬浮窗权限
     */
    private fun hasWindowPermission(context: Context): Boolean {
        val result = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                val appOpsMgr = context.getSystemService(Context.APP_OPS_SERVICE)
                if (appOpsMgr == null) {
                    false
                } else {
                    val mode = (appOpsMgr as AppOpsManager).checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                            .packageName)
                    mode == AppOpsManager.MODE_ALLOWED
                }

            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> Settings.canDrawOverlays(context)
            else -> true
        }
        LogUtils.instance.d("hasWindowPermission:$result")
        return result
    }

    /**
     * 获取json文件中标注的危险权限
     */
    @Throws(IOException::class)
    private fun getDangerPermission(context: Context): MutableMap<String, HashMap<String, List<String>>> {
        //获取json文件中的权限信息
        val input = context.resources.assets.open("permissions.json")
        val perContent = FileUtils.readFile2String(input, "UTF-8")
        return Gson().fromJson<HashMap<String, HashMap<String, List<String>>>>(perContent, object : TypeToken<HashMap<String, HashMap<String, List<String>>>>() {}.type)
    }

    /**
     * 获取权限的提示内容
     * @param permission
     * @param perMap
     */
    private fun getPermissionPrompt(permission: String, perMap: MutableMap<String, HashMap<String, List<String>>>): String {
        var prompt = ""
        perMap["danger_permissions"]?.values?.forEach {
            for (per: String in it) {
                if (per.contains(permission)) {
                    prompt = per.split(" ")[1]
                    break
                }
            }

        }
        return prompt
    }

    /**
     * 获取权限的提示信息
     * @param permissions 需要显示说明的权限
     * @return ArrayList<String> </String>
     */
    private fun getPermissionInfo(permissions: MutableList<String>): MutableList<String> {
        val infos = mutableListOf<String>()
        mPerMap?.let {
            for (per in permissions) {
                infos.add(getPermissionPrompt(per, it))
            }
        }
        return infos
    }

    /**
     * 请求单个权限后能够获取整组权限
     *
     * @param permissions 需要请求的权限
     * @param context    上下文
     * @param permissionListener 回调
     */
    fun requestPermissions(context: Context, permissions: MutableSet<String>?, permissionListener: PermissionListener) {
        mPermissions = permissions?.toTypedArray() ?: getAllPermissions(context)
        mPerListener = permissionListener
        mPermissions?.let {
            ActivityCompat.requestPermissions(context as Activity, it, REQUEST_CODE_ASK_PERMISSIONS)
        }
    }


    /**
     * 弹出权限设置提醒框
     * @param activity
     * @param title
     * @param permissions
     * @return
     */
    fun showPermissionDialog(activity: Activity, title: String, permissions: MutableSet<String>) {
        LogUtils.instance.d("showPermissionDialog")
        val sb = StringBuilder("您有已忽略的权限，请到设置中开启:\n\n")

        //获取危险权限信息
        for (per in permissions) {
            sb.append("\t\t\t\t").append(getPermissionPrompt(per, mPerMap!!)).append("\n")
        }
        NiceShowView.instance.createDialog(activity, NiceDialog.DIALOG_NORMAL)
                .setTitle(title)
                .setMessage(sb.toString())
                .setCanceled(mIsCancelable)
                .setCancel("取消", object : NiceDialog.DialogClickListener {
                    override fun onClick() {

                    }
                })
                .setConfirm("去设置", object : NiceDialog.DialogClickListener {
                    override fun onClick() {
                        startActivityToSetting(activity)
                    }
                }).show()
    }

    /**
     * 弹出权限设置提醒框
     * @param activity
     * @param title
     * @param message
     */
    fun showWindowDialog(activity: Activity, title: String, message: String) {
        LogUtils.instance.d("showWindowDialog")
        NiceShowView.instance.createDialog(activity, NiceDialog.DIALOG_NORMAL)
                .setTitle(title)
                .setMessage(message)
                .setCanceled(mIsCancelable)
                .setCancel("取消", object : NiceDialog.DialogClickListener {
                    override fun onClick() {
//                        destroy()
                    }
                })
                .setConfirm("去设置", object : NiceDialog.DialogClickListener {
                    override fun onClick() {
                        startActivityToOverlay(activity)
                    }
                }).show()
    }

    /**
     * 跳转到应用的设置界面
     * @param activity
     */
    private fun startActivityToSetting(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + AppUtils.instance.getPackageName())
        activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    /**
     * 跳转到应用的弹出框设置界面
     * @param activity
     */
    private fun startActivityToOverlay(activity: Activity) {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:" + AppUtils.instance.getPackageName())
            activity.startActivityForResult(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), REQUEST_CODE_ASK_PERMISSIONS)
        }
    }


    /**
     * 是否请求到权限
     * @param context
     * @param requestCode 请求码
     * @param permissions 请求的权限数组
     * @param grantResult 请求的结果码
     */
    fun handleRequestPermissionsResult(context: Context, requestCode: Int, permissions: Array<out String>, grantResult: IntArray) {
        LogUtils.instance.d("handleRequestPermissionsResult")
        val sb = StringBuilder("result:\n")
        for (i in permissions.indices) {
            sb.append(permissions[i]).append(":").append(grantResult[i]).append("\n")
        }
        LogUtils.instance.d("sb:$sb")
        var result = true
        val deniedPermission = mutableSetOf<String>()
        val grantedPermission = mutableSetOf<String>()
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> {
                for (i in grantResult.indices) {
                    result = result && grantResult[i] == PackageManager.PERMISSION_GRANTED
                    if (grantResult[i] != PackageManager.PERMISSION_GRANTED) {
                        //未通过的权限
                        when {
                            "android.permission.SYSTEM_ALERT_WINDOW" == permissions[i] -> if (mHasWindow && !hasWindowPermission(context)) deniedPermission.add(permissions[i])
                            isDangerPermission(permissions[i]) -> deniedPermission.add(permissions[i])
                            else -> LogUtils.instance.d("not dangerous:${permissions[i]}")
                        }
                    } else {
                        grantedPermission.add(permissions[i])
                    }
                }
                val noPermissions = getNoPermission(context, grantedPermission)
                LogUtils.instance.d("noPermissions$noPermissions")
                //是否请求到全部权限的
                mPerListener?.deniedCallback(deniedPermission)
                mPerListener?.ignoredCallback(checkIgnorePermissions(context, deniedPermission))
                mPerListener?.grantedCallback(grantedPermission)
                mPerListener = null
            }
        }
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
        val audioRecord: AudioRecord? = AudioRecord(audioSource, sampleRateInHz,
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
        return true
    }

    interface PermissionListener {
        /**
         * 权限请求成功的回调
         */
        fun grantedCallback(permissions: MutableSet<String>)

        /**
         * 权限请求失败的回调
         */
        fun deniedCallback(permissions: MutableSet<String>)

        /**
         * 权限请求忽略的回调
         */
        fun ignoredCallback(permissions: MutableSet<String>)

    }

}



