package com.jplus.manyfunction.utils;//package com.jplus.manyfunction.utils;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Build;
//import android.provider.Settings;
//
//import com.nice.baselibrary.base.utils.FileUtils;
//import com.nice.baselibrary.base.utils.LogUtils;
//import com.nice.baselibrary.base.utils.ShowViewHelperKt;
//import com.nice.baselibrary.widget.dialog.NiceDialog;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//
///**
// * @author JPlus
// * @date 2019/3/27.
// */
//
//public class NicePermissionUtils {
//    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1024;
//    private static final int TO_SETTING_SET_PERMISSIONS = 2048;
//    private static NicePermissionUtils mNicePermission;
//
//    public static NicePermissionUtils getInstance() {
//        if (mNicePermission == null) {
//            mNicePermission = new NicePermissionUtils();
//        }
//        return mNicePermission;
//    }
//    private Context mContext;
//    private NiceDialog mNiceDialog;
//    private boolean mOpenWindow;
//    private boolean mIsCancelable;
//
//
//    public void init(Context context, boolean openWindow, boolean isCancelable) {
//        mContext = context;
//        mOpenWindow = openWindow;
//        mIsCancelable = isCancelable;
//    }
//    public void init(Context context, boolean openWindow) {
//        init(context, openWindow, true);
//    }
//
//    public void init(Context context) {
//        init(context, true);
//    }
//
//    /**
//     * 获取应用注册的权限
//     * @param context 上下文
//     * @return 应用在manifest中注册的权限数组
//     */
//    private String[] getAllPermissions(Context context) {
//        String[] permissions = null;
//        try {
//            //此列表包括所有请求的权限，甚至包括系统在安装时未授予或已知的权限
//            permissions = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e.getMessage(), e);
//        }
//        return permissions;
//    }
//
//    /**
//     * 检查是否全部通过权限
//     *
//     * @param params 应用的权限数组
//     * @return 未通过的权限数组
//     */
//    private String[] getNoPermission(String[] params) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || params == null || params.length == 0) {
//            return null;
//        }
//        ArrayList<String> noPermissionList = new ArrayList<>();
//        StringBuilder sb = new StringBuilder("getNoPermission").append("\n");
//        for (String permission : params) {
//            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
//                noPermissionList.add(permission);
//                sb.append(permission).append("\n");
//            }
//        }
//        LogUtils.INSTANCE.d(sb.toString(), "tag");
//        String[] noPermissions = new String[noPermissionList.size()];
//        for (int i = 0; i < noPermissionList.size(); i++) {
//            noPermissions[i] = noPermissionList.get(i);
//        }
//        return noPermissions;
//    }
//
//    /**
//     * 是否有忽略的权限
//     *
//     * @param params 未通过的权限数组
//     * @return 被忽略的权限数组
//     */
//    private String[] shouldShowRequestPermissions(String[] params) {
//        if (params == null || params.length == 0) {
//            return null;
//        }
//        ArrayList<String> ignorePermissionList = new ArrayList<>();
//        StringBuilder sb = new StringBuilder("shouldShowRequestPermissions").append("\n");
//        for (String noPermission : params) {
//            if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, noPermission)) {
//                ignorePermissionList.add(noPermission);
//                sb.append(noPermission).append("\n");
//            }
//        }
//
//        LogUtils.INSTANCE.d(sb.toString(), "tag");
//        String[] ignorePermissions = new String[ignorePermissionList.size()];
//        for (int i = 0; i < ignorePermissionList.size(); i++) {
//            ignorePermissions[i] = ignorePermissionList.get(i);
//        }
//        return ignorePermissions;
//    }
//
//    /**
//     * 请求所有权限
//     */
//    public void requestPermissions() {
//        requestPermissions(mContext);
//    }
//
//    /**
//     * 请求所有权限
//     *
//     * @param context
//     */
//    public void requestPermissions(Context context) {
//        LogUtils.INSTANCE.e("requestPermissions", "manyfunction");
//        String[] mNoPermission = getNoPermission(getAllPermissions(context));
//        if (mNoPermission != null && mNoPermission.length != 0) {
//            ActivityCompat.requestPermissions((Activity) context, mNoPermission, REQUEST_CODE_ASK_PERMISSIONS);
//        }
//    }
//
//    /**
//     * 请求单个权限后能够获取整组权限
//     *
//     * @param permission 需要请求的权限
//     */
//    public void requestPermissions(String permission) {
//        requestPermissions(permission, mContext);
//    }
//
//    /**
//     * 请求单个权限后能够获取整组权限
//     *
//     * @param permission 需要请求的权限
//     * @param context 上下文
//     */
//    public void requestPermissions(String permission, Context context) {
//        if (permission == null) {
//            return;
//        }
//        String[] permissions = {permission};
//        String[] noPermissions = getNoPermission(permissions);
//        if (permissions.length != 0 && noPermissions != null && noPermissions.length != 0) {
//            ActivityCompat.requestPermissions((Activity) context, permissions, REQUEST_CODE_ASK_PERMISSIONS);
//        }
//    }
//
//    /**
//     * 弹出权限设置提醒框
//     *
//     * @param context 上下文
//     * @param permissions 需要显示说明的权限
//     * @return 权限弹出框
//     */
//    private NiceDialog showPermissionDialog(final Context context, String[] permissions) {
//        LogUtils.INSTANCE.e("showPermissionDialog", "manyfunction");
//        StringBuilder sb = new StringBuilder("您有已忽略的权限，请到设置中开启:\n");
//        ArrayList<String> pertxt= new ArrayList<>();
//        try {
//            InputStream input = context.getResources().getAssets().open("permissions.json");
//            pertxt = FileUtils.Companion.readFile2List(input, "UTF-8");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for(String per:permissions){
//            for(String per2:pertxt){
//                if(per2.contains(per)){
//                    sb.append("\t\t").append(per2.split(":")[0]).append("\n");
//                }
//            }
//        }
//        return ShowViewHelperKt.createDialog(NiceDialog.Companion.getDIALOG_NORMAL())
//                .setTitle("关于权限")
//                .setMessage(sb.toString())
//                .setCanceled(mIsCancelable)
//                .setCancel("取消", new NiceDialog.DialogClickListener() {
//                    @Override
//                    public void onClick() {
//                        destroy();
//                    }
//                })
//                .setConfirm("去设置", new NiceDialog.DialogClickListener() {
//                    @Override
//                    public void onClick() {
//                        startActivityToSetting((Activity) context);
//                    }
//                });
//    }
//    /**
//     * 跳转到应用的设置界面
//     *
//     * @param activity
//     */
//    private void startActivityToSetting(Activity activity) {
//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        intent.setData(Uri.parse("package:" + activity.getApplicationInfo().packageName));
//        activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//    }
//
//
//    /**
//     * 跳转到应用的悬浮窗设置界面
//     *
//     * @param activity
//     */
//    private void startActivityToOverlay(Activity activity) {
//        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//        intent.setData(Uri.parse("package:" + activity.getApplicationInfo().packageName));
//        activity.startActivityForResult(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), REQUEST_CODE_ASK_PERMISSIONS);
//    }
//
//    /**
//     * 避免内存泄漏
//     */
//     public void destroy() {
//        if(mContext!=null){
//            mContext = null;
//        }
//        if (mNiceDialog != null) {
//            mNiceDialog.dismiss();
//            mNiceDialog = null;
//        }
//    }
//    /**
//     * 是否请求到权限
//     * @param requestCode
//     * @param permissions
//     * @param grantResult
//     */
//    public void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
//        handleRequestPermissionsResult(mContext, requestCode, permissions, grantResult);
//    }
//
//    /**
//     * 是否请求到权限
//     * @param context 上下文
//     * @param requestCode 请求码
//     * @param permissions 请求的权限数组
//     * @param grantResult 请求的结果码
//     */
//    public void handleRequestPermissionsResult(Context context, int requestCode, String[] permissions, int[] grantResult) {
//        boolean result = true;
//        ArrayList<String> noPermissionList = new ArrayList<>();
//        switch (requestCode) {
//            case REQUEST_CODE_ASK_PERMISSIONS:
//                for (int i = 0; i < grantResult.length; i++) {
//                    result = result && grantResult[i] == PackageManager.PERMISSION_GRANTED;
//                    if (grantResult[i] != PackageManager.PERMISSION_GRANTED) {
//                        //未通过的权限
//                        noPermissionList.add(permissions[i]);
//                    }
//                }
//                //是否请求到全部权限的
//                if (result) {
//                    LogUtils.INSTANCE.d("success", "tag");
//                } else {
//                    LogUtils.INSTANCE.d("failed", "tag");
//                }
//                break;
//            default:
//                break;
//        }
//
//        String[] noPermissions = new String[noPermissionList.size()];
//        for (int i = 0; i < noPermissionList.size(); i++) {
//            noPermissions[i] = noPermissionList.get(i);
//        }
//        String[] ignorePermissions = shouldShowRequestPermissions(noPermissions);
//        if (ignorePermissions != null && ignorePermissions.length != 0) {
//            if (mOpenWindow) {
//                if(mNiceDialog==null) {
//                    mNiceDialog = showPermissionDialog(context, ignorePermissions);
//                }
//                mNiceDialog.show();
//            }
//        }
//        StringBuilder sb = new StringBuilder("handleRequestPermissionsResult").append("\n");
//        for (String per : noPermissions) {
//            sb.append(per).append("\n");
//        }
//        sb.append("" + requestCode + "\n");
//        for (int grant : grantResult) {
//            sb.append("" + grant).append("\n");
//        }
//        LogUtils.INSTANCE.d(sb.toString(), "manyfunction");
//    }
//
//}
