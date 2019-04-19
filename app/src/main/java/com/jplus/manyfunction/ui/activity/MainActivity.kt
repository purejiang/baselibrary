package com.jplus.manyfunction.ui.activity


import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import com.jplus.assist.sdk.base.Assist
import com.jplus.manyfunction.R
import com.nice.baselibrary.base.adapter.NiceAdapter
import com.nice.baselibrary.base.ui.NiceActivity
import com.nice.baselibrary.base.common.ApiEntry
import com.nice.baselibrary.base.utils.*
import com.nice.baselibrary.base.ui.view.NiceDialog
import com.nice.baselibrary.base.ui.view.NiceShowView
import com.nice.baselibrary.base.ui.view.NiceTextView
import com.nice.baselibrary.base.ui.view.dialog.NiceAlertDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : NiceActivity(){
//    private var mTitleBar: NiceTitleBar?=null
    private var niceDialog: NiceAlertDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        niceDialog = NiceShowView.getInstance().createDialog(this, NiceDialog.DIALOG_SMALLER)
//                .setTitle("加载")
//                .setCanceled(false)
//                .setCircleProgress("loading...")
//        niceDialog?.show()
        test_btn.setOnClickListener {
            niceDialog = NiceAlertDialog.Builder(supportFragmentManager.beginTransaction())
                    .setBackgroundRes(R.drawable.bg_normal_dialog_view)
                    .setLayoutRes(R.layout.view_photo_dialog)
                    .setCancelable(false)
                    .setTag("newDialog")
                    .setScreenHeightPercent(this, 0.2f)
                    .setScreenWidthPercent(this, 1.0f)
                    .setAnimationRes(R.style.NiceDialogAnim)
                    .setGravity(Gravity.BOTTOM)
                    .setDimAmount(0.0f)
                    .setBindViewListener(object :NiceAlertDialog.OnBindViewListener{
                        override fun onBindView(viewHolder: NiceAdapter.VH) {
                            viewHolder.getView<NiceTextView>(R.id.ntv_photo_dialog_camera).text ="相机"
                            viewHolder.getView<NiceTextView>(R.id.ntv_photo_dialog_photo).text ="照片"
                            viewHolder.getView<NiceTextView>(R.id.ntv_photo_dialog_cancel).text ="取消"
//                            viewHolder.getView<NiceTextView>(R.id.btv_dialog_confirm).text ="确认"
//                            viewHolder.getView<NiceTextView>(R.id.btv_dialog_cancel).text ="取消"
//                            viewHolder.getView<NiceTextView>(R.id.btv_dialog_message).text ="这是个测试的dialog"
                        }
                    })
//                    .addClickedId(R.id.btv_dialog_confirm, R.id.btv_dialog_cancel, R.id.btv_dialog_message)
                    .addClickedId(R.id.ntv_photo_dialog_camera, R.id.ntv_photo_dialog_photo, R.id.ntv_photo_dialog_cancel)
                    .setViewClickListener(object :NiceAlertDialog.OnViewClickListener{
                        override fun onClick(viewHolder: NiceAdapter.VH, view: View, dialog: NiceAlertDialog) {
                            when(view.id){
                                R.id.ntv_photo_dialog_camera -> NiceShowView.getInstance().NormalToast("相机").show()
                                R.id.ntv_photo_dialog_photo -> NiceShowView.getInstance().NormalToast("照片").show()
                                R.id.ntv_photo_dialog_cancel -> niceDialog?.dismiss()
//                                R.id.btv_dialog_confirm -> NiceShowView.getInstance().NormalToast("confirm").show()
//                                R.id.btv_dialog_cancel -> NiceShowView.getInstance().NormalToast("cancel").show()
//                                R.id.btv_dialog_message -> NiceShowView.getInstance().NormalToast("message").show()
                            }
                        }
                    })
                    .setKeyListener(object :DialogInterface.OnKeyListener{
                        override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                NiceShowView.getInstance().NormalToast("back failed").show()
                                return true
                            }
                            return  false
                        }
                    })

                    .create()
                    .show()
        }



//        Assist.init(this)
//        Assist.getAutoNetWork(false).SetOnNetWorkChangeListener(object :NetWorkReceiver.OnNetWorkChangeListener{
//            override fun getNetStatus(isOnline: Boolean) {
//                LogUtils.getInstance().e("isOnline:"+isOnline)
//            }
//
//            override fun getNetType(type: Int) {
//                val message = when(type){
//                    NetWorkReceiver.NETWORK_MOBILE -> "移动网络已连接！"
//                    NetWorkReceiver.NETWORK_WIFI -> "Wifi已连接"
//                    NetWorkReceiver.NETWORK_INVALID ->"网络未连接"
//                    else -> {
//                        "未知网络类型"
//                    }
//                }
//                NiceShowView.getInstance().NormalToast(message).show()
////                niceDialog?.dismiss()
//            }
//
//            override fun getNetStrength(strength: Int) {
//                LogUtils.getInstance().e("strength:"+strength)
//            }
//        })

//        val fragment = NiceDialogFragment.newInstance()
//        fragment.show(supportFragmentManager, "alertDialog")
//        val timer = Timer()
//        timer.schedule(object : TimerTask() {
//            override fun run() {
//                niceDialog?.dismiss()
//            }
//        },10000)
//        NicePermissionUtils.getInstance().init(this ,true)
//        val mSpUtils = SharePreferenceUtils.create(this)
//        val appInfoList = AppUtils.getInstance().getAppsInfo( false)
//        val sb = StringBuilder("appInfos:")
//        for (info:AppInfo in appInfoList){
//            sb.append(info.appName+":"+info.signMd5).append("\n")
//        }
//        LogUtils.getInstance().d(sb.toString())
//        if(!mSpUtils.contains("notFirstOpen")){
//           mSpUtils.set("notFirstOpen", true)
//        }else{
//            val file1 = File(Constant.Companion.Path.ROOT_DIR, "cc.txt")
//            LogUtils.getInstance().e(FileUtils.getFileMD5(file.absolutePath))

//            val file = File(Constant.Companion.Path.ROOT_DIR, "xUtils.db")
//            FileUtils.writeFile(file1, FileUtils.readFile2String(file, "UTF-8"), false)
//            val db = SQLiteDatabase.openOrCreateDatabase(file.absolutePath, null)
//            val cursor = db.rawQuery("select name from sqlite_sequence",null)
//            while (cursor.moveToNext()) {
//                //遍历出表名
//                val name = cursor.getString(0)
//                LogUtils.getInstance().e("table:"+name)
//                val cursor = db.rawQuery("select * from $name",null)
//                while(cursor.moveToNext()){
//                    for(a  in cursor.columnNames){
//                        LogUtils.getInstance().e("columnNames:"+a)
//                    }
//
//                }

//            }
//            cursor.close()
//            val input = ExcCommand.exc("sqlite3 "+file.absolutePath)
//            Class.forName("org.sqlite.JDBC")
//            val connect  = DatabaseMetaData.
//            LogUtils.getInstance().e(input)
//            LogUtils.getInstance().saveLog()
////            startActivity(Intent(this, DownloadListActivity::class.java))
//            finish()
//        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

    }


    override fun onInit() {

    }

    override fun onStart() {
        super.onStart()

//        NicePermissionUtils.getInstance().requestPermissions()
        ApiEntry.getInstance().requestPermission(this)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }
    override fun onDestroy() {
        super.onDestroy()
        ApiEntry.getInstance().destroy()
        Assist.unRegisterNetReceiver()
//        NicePermissionUtils.getInstance().destroy()
    }
    override fun onFindView() {
//        mTitleBar = findViewById(R.id.base_tb_main)
//        mTitleBar?.setTitleBarColor(R.color.colorAccent)

        base_tb_main?.setTitleBarColor(R.color.colorAccent)
    }

    override fun onBindListener() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.getInstance().d("request"+requestCode, "tag")
        LogUtils.getInstance().d("result"+resultCode, "tag")
        LogUtils.getInstance().d("data"+data?.data, "tag")
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        ApiEntry.getInstance().handleRequestPermissionsResult(requestCode, permissions, grantResults)
//        NicePermissionUtils.getInstance().handleRequestPermissionsResult( requestCode, permissions, grantResults)
    }
    override fun isBackTwice():Boolean{
        return false
    }
}

