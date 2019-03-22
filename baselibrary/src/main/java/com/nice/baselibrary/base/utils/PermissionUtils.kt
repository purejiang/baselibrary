package com.nice.baselibrary.base.utils

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
import com.nice.baselibrary.base.view.BaseShowView


/**
 * 动态权限工具类
 * @author JPlus
 * @date 2019/2/27.
 */
class PermissionUtils private constructor() {

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
    private fun getPermissions(context: Context): Array<String>? {
        return try {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)?.requestedPermissions //此列表包括所有请求的权限，甚至包括系统在安装时未授予或已知的权限
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            arrayOf()
        }

    }

    /**
     * 检查是否全部通过权限
     * @return 未通过的权限列表
     */
    private fun hasPermission(params: Array<String>?): Array<String> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || params == null || params.isEmpty()) {
            return arrayOf()
        }
        val sb = StringBuilder("hasPermission").append("\n")
        val noPermission = ArrayList<String>()
        params.filter { ContextCompat.checkSelfPermission(mContext!!, it) != PackageManager.PERMISSION_GRANTED }//筛选未通过的权限
                .forEach {
                    noPermission.add(it)
                    sb.append(it).append("\n")
                }
        LogUtils.getInstance().d(sb.toString())
        return noPermission.toArray(Array(noPermission.size, { "" }))
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
        val ignorePermissions = ArrayList<String>()
        params.filter { !ActivityCompat.shouldShowRequestPermissionRationale(mContext as Activity, it) }//筛选被用户忽略的权限
                .forEach {
                    ignorePermissions.add(it)
                    sb.append(it).append("\n")
                }
        LogUtils.getInstance().d(sb.toString())
        return ignorePermissions.toArray(Array(ignorePermissions.size, { "" }))//返回数组
    }

    /**
     * 跳转到应用的设置界面
     * @param activity
     * @return
     */
    private fun startActivityToSetting(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + AndroidUtils.getPackageName(mContext))
        activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    /**
     * 跳转到应用的弹出框设置界面
     * @param activity
     * @return
     */
    private fun startActivityToOverlay(activity: Activity) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.parse("package:" + AndroidUtils.getPackageName(mContext))
        activity.startActivityForResult(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), REQUEST_CODE_ASK_PERMISSIONS)
    }


    /**
     * 请求权限
     */
    fun requestPermissions() {
        mNoPermission = hasPermission(getPermissions(mContext!!))
        if (mNoPermission != null && !mNoPermission!!.isEmpty()) {
            ActivityCompat.requestPermissions(mContext as Activity, mNoPermission!!, REQUEST_CODE_ASK_PERMISSIONS)
        }else{
            destroy()
        }
    }

    /**
     * 弹出权限设置提醒框
     * @param context
     */
    private fun showPermissionDialog(context: Context): NiceDialog {
        return BaseShowView.getInstance().baseDialog(context)
                .setTitle("关于权限")
                .setMessage("您有未同意的权限，请到设置中开启！")
                .setConfirm("去设置", object : NiceDialog.DialogClickListener {
                    override fun onClick() {
//                        startActivityToOverlay(context as Activity)
                        startActivityToSetting(context as Activity)
                    }
                })
    }

    private fun destroy(){
        if(mPermissionDialog!=null){
            mPermissionDialog?.dismiss()
            mPermissionDialog = null
        }
    }

    /**
     * 是否请求到权限
     * @param requestCode
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
        }else{
            destroy()
        }
    }
}


