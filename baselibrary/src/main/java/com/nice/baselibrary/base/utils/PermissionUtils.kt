package com.nice.baselibrary.base.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.nice.baselibrary.base.view.NiceDialog
import com.nice.baselibrary.base.view.NiceShowView


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
        val CAMERA = Manifest.permission.CAMERA
        val READ_CONTACTS = Manifest.permission.READ_CONTACTS
//        val ACCESS_FINE_LOCATION = Manifest.permission.read
        @Synchronized
        fun getInstance(): PermissionUtils {
            if (mPermissionUtils == null) {
                mPermissionUtils = PermissionUtils()
            }
            return mPermissionUtils!!
        }

    }

    private var mContext: Context? = null
    private var mNoPermission: Array<String>? = null
    private var mIgnorePermissions: Array<String>? = null
    private var mPermissionDialog: NiceDialog? = null
    fun init(context: Context) {
        mContext = context
    }


    /**
     * 获取应用注册的权限
     * @param context
     * @return
     */
    private fun getAllPermission(context: Context): Array<String>? {
        return try {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)?.requestedPermissions //此列表包括所有请求的权限，甚至包括系统在安装时未授予或已知的权限
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            arrayOf()
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
        mNoPermission = getNoPermission(getAllPermission(mContext!!))
        if (mNoPermission != null && !mNoPermission!!.isEmpty()) {
            ActivityCompat.requestPermissions(mContext as Activity, mNoPermission!!, REQUEST_CODE_ASK_PERMISSIONS)
        } else {
            destroy()
        }
    }

    /**
     * 请求单个权限后能够获取整组权限
     */
    fun requestPermissions(permission: String) {
        val permissions = arrayOf(permission)
        if (!permissions.isEmpty() && !getNoPermission(permissions).isEmpty()) {
            ActivityCompat.requestPermissions(mContext as Activity, permissions, REQUEST_CODE_ASK_PERMISSIONS)
        } else {
            destroy()
        }
    }

    /**
     * 弹出权限设置提醒框
     * @param context
     * @return
     */
    private fun showPermissionDialog(context: Context): NiceDialog {
        return NiceShowView.getInstance().baseDialog(context)
                .setTitle("关于权限")
                .setMessage("您有未同意的权限，请到设置中开启！")
                .setConfirm("去设置", object : NiceDialog.DialogClickListener {
                    override fun onClick() {
//                        startActivityToOverlay(context as Activity)
                        startActivityToSetting(context as Activity)
                    }
                })
    }

    private fun destroy() {
        if (mPermissionDialog != null) {
            mPermissionDialog?.dismiss()
            mPermissionDialog = null
        }
    }

    /**
     * 是否请求到权限
     * @param requestCode
     * @param permissions
     * @param grantResult
     */
    fun handleRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResult: IntArray) {
        var result = false
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> {
                grantResult.filter { it == PackageManager.PERMISSION_GRANTED }
                        .forEach { result = true }
                if (result) {
                    //请求到权限的
                    LogUtils.getInstance().d("success")
                } else {
                    //未请求到权限的
                    LogUtils.getInstance().d("failed")
                }
            }
        }
        val sb = StringBuilder("handleRequestPermissionsResult").append("\n")
        for (per: String in permissions) {
            sb.append(per).append("\n")
        }
        LogUtils.getInstance().d(sb.toString())
        if (!mNoPermission!!.isEmpty() && !shouldShowRequestPermissions(mNoPermission!!).isEmpty()) {
            destroy()
            mPermissionDialog = showPermissionDialog(mContext!!)
            mPermissionDialog?.show()
            LogUtils.getInstance().d("mPermissionDialog is " + (mPermissionDialog == null).toString())
            LogUtils.getInstance().d("show")
        } else {
            destroy()
        }
    }
}


