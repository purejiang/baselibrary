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
import com.nice.baselibrary.base.ui.view.dialog.NiceDialog
import com.nice.baselibrary.base.ui.view.NiceShowView
import java.io.IOException
import java.lang.RuntimeException


/**
 * 动态权限工具类
 * @author JPlus
 * @date 2019/2/27.
 */
class NicePermissions private constructor() {

    companion object {
        private const val REQUEST_CODE_ASK_PERMISSIONS = 1024

        private var mPermissionUtils: NicePermissions? = null
        fun getInstance(): NicePermissions {
            if (mPermissionUtils == null) {
                synchronized(NicePermissions::class.java) {
                    mPermissionUtils = NicePermissions()
                }
            }
            return mPermissionUtils!!
        }
        /**
         * 跳转到应用的设置界面
         * @param activity
         */
         fun startActivityToSetting(activity: Activity) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:" + AppUtils.getInstance().getPackageName())
            activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }

        /**
         * 跳转到应用的弹出框设置界面
         * @param activity
         */
        fun startActivityToOverlay(activity: Activity) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:" + AppUtils.getInstance().getPackageName())
            activity.startActivityForResult(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), REQUEST_CODE_ASK_PERMISSIONS)
        }
    }
    private var mNiceDialog: NiceDialog? = null
    private var mOpenWindow: Boolean = false
    private var mIsCancelable: Boolean = false
    private var mPerMap: MutableMap<String, HashMap<String, List<String>>>? = null
    private var mPer2Listener:MutableMap<MutableSet<String>, PermissionListener>?=null
    private var mPerListener:PermissionListener?=null
    private var mContext:Context?=null
    private var mAllPermissions:Array<String>?= null

    fun init(context: Context, openWindow: Boolean, isCancelable: Boolean) {
        mOpenWindow = openWindow
        mIsCancelable = isCancelable
        mPer2Listener = HashMap()
        mAllPermissions = getAllPermissions(context)
        mPerMap = getDangerPermission(context)
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
    private fun getNoPermission(context: Context, params: Array<String>?): Array<String> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || params == null || params.isEmpty()) {
            return arrayOf()
        }
        val sb = StringBuilder("getNoPermission").append("\n")
        val noPermission: MutableList<String> = ArrayList()
        params.filter { ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED && isDangerPermission(it) }//筛选未通过的权限
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
    private fun checkIgnorePermissions(context: Context, params: Array<String>): Array<String> {
        val sb = StringBuilder("shouldShowRequestPermissions").append("\n")
        if (params.isEmpty()) {
            return arrayOf()
        }
        val ignorePermissions: MutableList<String> = ArrayList()
        params.filter { !ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, it) && isDangerPermission(it) }//筛选被用户忽略的权限
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
    private fun isDangerPermission(permission: String): Boolean {
        val dangerPermission = mPerMap!!["danger_permissions"]
        for (perInfoList: List<String> in dangerPermission?.values!!) {
            perInfoList
                    .filter { it.contains(permission) }
                    .forEach {
                        LogUtils.getInstance().e(it)
                        return true
                    }
        }
        return false
    }

    /**
     * 获取json文件中标注的危险权限
     */
    @Throws(IOException::class)
    private fun getDangerPermission(context: Context):MutableMap<String, HashMap<String, List<String>>>{
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
    private fun getPermissionPrompt(permission: String, perMap:MutableMap<String, HashMap<String, List<String>>> ):String{
        var prompt = ""
        perMap["danger_permissions"]?.values?.forEach{
            for(per:String in it){
                if (per.contains(permission)){
                    prompt = per.split(" ")[1]
                    break
                }
            }

        }
        return prompt
    }


    /**
     * 请求权限
     * @param context
     * @param permissions
     * @param permissionListener
     */
    fun requestPermissions(context: Context, permissions: MutableSet<String>?, permissionListener:PermissionListener) {
        mContext =context
        mPerListener = permissionListener

        LogUtils.getInstance().d("requestPermissions")
            //判断请求权限是否为空
            val mNoPermission = if(permissions!=null&&permissions.isNotEmpty()){
//                var isExist = false //是否存在该权限
//                mPer2Listener?.keys?.forEach {
//                    isExist = isExist || it.containsAll(permissions)
//                }
//                if (!isExist) mPer2Listener?.let { it[permissions] = permissionListener }  //设置相应权限组的监听
//
//                //返回指定请求的权限
                permissions.toTypedArray()
            } else{
                //返回所有权限
                mAllPermissions!!
            }

            if (mNoPermission.isNotEmpty()) {
                ActivityCompat.requestPermissions(context as Activity, mNoPermission, REQUEST_CODE_ASK_PERMISSIONS)
            }
        }



    /**
     * 弹出权限设置提醒框
     * @param context
     * @param permissions
     * @return
     */
    private fun showPermissionDialog(context: Context, permissions: Array<String>): NiceDialog {
        LogUtils.getInstance().e("showPermissionDialog")
        val sb = StringBuilder("您有已忽略的权限，请到设置中开启:\n\n")

        //获取危险权限信息
        for (per in permissions) {
            sb.append("\t\t\t\t").append(getPermissionPrompt(per, mPerMap!!)).append("\n")
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
        if(mContext!=null){
            mContext=null
        }
        if (mNiceDialog != null) {
            mNiceDialog?.dismiss()
            mNiceDialog = null
        }
    }

    /**
     * 是否请求到权限
     * @param requestCode 请求码
     * @param permissions 请求的权限数组
     * @param grantResult 请求的结果码
     */
    fun handleRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResult: IntArray) {
        LogUtils.getInstance().e("handleRequestPermissionsResult")
        var result = true
        val grantedList:MutableList<String> = ArrayList()
        val deniedList:MutableList<String> = ArrayList()
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> {
                for (i in grantResult.indices) {
                    result = result && grantResult[i] == PackageManager.PERMISSION_GRANTED
                    if (grantResult[i] != PackageManager.PERMISSION_GRANTED) {
                        //权限未通过
                        deniedList.add(permissions[i])
                        LogUtils.getInstance().d("------"+permissions[i]+"::failed")
                    }else{
                        //权限通过
                        grantedList.add(permissions[i])
                        LogUtils.getInstance().d("------"+permissions[i]+"::success")
                    }
                }

                //已同意的权限回调
//                for(per:String in grantedList){
                    mPerListener?.grantedCallback(grantedList)
//                }
                //未同意的权限回调
//                for(per:String in deniedList){
                LogUtils.getInstance().e("handleRequestPermissionsResult")
                    mPerListener?.deniedCallback(deniedList)
//                }
                //result是否请求到全部权限的
//                mPerListener?.isRequestAll(result)
            }
        }
    }
    /**
     * 遍历储存权限监听的Map
     * @param context
     * @param per2Listener
     * @param permissions
     */
    private fun setListener(context: Context, per2Listener:MutableMap<MutableSet<String>, PermissionListener>, permissions: MutableSet<String>){
        //遍历储存权限监听的Map
        per2Listener.keys.forEach{
            //查找是否包含权限组
            if(it.containsAll(permissions.toMutableSet())){
                val no = getNoPermission(context, it.toTypedArray()) //未通过的权限
                val yes = it.subtract(no.toMutableSet())//通过subtract()方法得出差集，获取已通过的权限

                if(yes.isNotEmpty()) {
                    for (permission: String in yes) {
//                        per2Listener[it]?.grantedCallback(permission)
                    }
                }
                if(no.isNotEmpty()) {
                    for (permission: String in no) {
//                        per2Listener[it]?.deniedCallback(permission)
                    }
                }

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
        return true
    }
    interface PermissionListener{
        /**
         * 权限请求成功的回调
         */
        fun grantedCallback(permissions:MutableList<String>)

        /**
         * 权限请求失败的回调
         */
        fun deniedCallback(permissions:MutableList<String>)



    }

    interface PermissionsCallBack {
        /**
         * 成功的回调
         * @param permissions
         */
        fun permissionGrantedCallback(permissions: Array<out String>)

        /**
         * 失败的回调
         * @param permissions
         */
        fun permissionDeniedCallback(permissions: Array<out String>)

        /**
         * 忽略的回调,只提供危险权限
         * @param permissions
         */
        fun permissionIgnoreCallback(permissions: Array<out String>)
    }


}



