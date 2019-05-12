package com.jplus.manyfunction.ui.activity


import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.jplus.manyfunction.R
import com.nice.baselibrary.base.adapter.NiceAdapter
import com.nice.baselibrary.base.common.ApiEntry
import com.nice.baselibrary.base.ui.NiceActivity
import com.nice.baselibrary.base.ui.view.NiceShowView
import com.nice.baselibrary.base.ui.view.NiceTextView
import com.nice.baselibrary.base.ui.view.dialog.NiceAlertDialog
import com.nice.baselibrary.base.ui.view.dialog.NiceDialog
import com.nice.baselibrary.base.utils.LogUtils
import com.nice.baselibrary.base.utils.NicePermissions
import com.nice.baselibrary.base.utils.PhotoUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : NiceActivity() {

    private var niceDialog: NiceAlertDialog? = null
    private var mPhotoUtils: PhotoUtils? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPhotoUtils = PhotoUtils(this@MainActivity)
        var isCanOpen = true
        btn_camera_test.setOnClickListener {
            niceDialog = NiceAlertDialog.Builder(supportFragmentManager.beginTransaction())
                    .setBackgroundRes(R.drawable.bg_normal_dialog_view)
                    .setLayoutRes(R.layout.view_photo_dialog)
                    .setCancelable(true)
                    .setTag("newDialog")
                    .setScreenHeightPercent(this, 0.2f)
                    .setScreenWidthPercent(this, 1.0f)
                    .setAnimationRes(R.style.NiceDialogAnim)
                    .setGravity(Gravity.BOTTOM)
                    .setDimAmount(0.0f)
                    .setBindViewListener(object : NiceAlertDialog.OnBindViewListener {
                        override fun onBindView(viewHolder: NiceAdapter.VH) {
                            viewHolder.getView<NiceTextView>(R.id.ntv_photo_dialog_camera).text = "相机"
                            viewHolder.getView<NiceTextView>(R.id.ntv_photo_dialog_photo).text = "照片"
                            viewHolder.getView<NiceTextView>(R.id.ntv_photo_dialog_cancel).text = "取消"

                        }
                    })
                    .addClickedId(R.id.ntv_photo_dialog_camera, R.id.ntv_photo_dialog_photo, R.id.ntv_photo_dialog_cancel)
                    .setViewClickListener(object : NiceAlertDialog.OnViewClickListener {
                        override fun onClick(viewHolder: NiceAdapter.VH, view: View, dialog: NiceAlertDialog) {
                            niceDialog?.dismiss()
                            ApiEntry.getInstance().requestPermission(this@MainActivity, mutableSetOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), object : NicePermissions.PermissionListener {
                                override fun GrantedCallback(permissions: String) {


                                }

                                override fun DeniedCallback(permissions: String) {
                                    when (permissions) {
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE ->{
                                            isCanOpen = isCanOpen && false
                                            showDialog("请开启读写手机存储权限")
                                        }
                                        Manifest.permission.CAMERA ->{
                                            isCanOpen = isCanOpen && false
                                            showDialog("请开启相机权限")
                                        }
                                    }
                                }

                            })
                            when (view.id) {
                                R.id.ntv_photo_dialog_camera -> {
                                    if (isCanOpen) {
                                        mPhotoUtils?.openCamera("pic_" + System.currentTimeMillis(), true,
                                                object : PhotoUtils.ChoosePictureCallback {
                                                    override fun onSuccess(uri: Uri) {
                                                        head_image.setImageBitmap(BitmapFactory.decodeStream(contentResolver.openInputStream(uri)))
                                                    }

                                                    override fun onFail() {
                                                        NiceShowView.getInstance().NormalToast("读取照片失败~").show()
                                                    }

                                                }, 1006, 1007)
                                    }
                                }
                                R.id.ntv_photo_dialog_photo ->{
                                    if (isCanOpen) {
                                        mPhotoUtils?.openPhoto(true,
                                                object : PhotoUtils.ChoosePictureCallback {
                                                    override fun onSuccess(uri: Uri) {
                                                        head_image.setImageBitmap(BitmapFactory.decodeStream(contentResolver.openInputStream(uri)))
                                                    }

                                                    override fun onFail() {
                                                        NiceShowView.getInstance().NormalToast("读取照片失败~").show()
                                                    }

                                                }, 1008, 1009)
                                    }
                                }
                                R.id.ntv_photo_dialog_cancel -> niceDialog?.dismiss()
                            }
                        }
                    })
                    .setKeyListener(DialogInterface.OnKeyListener { _, _, _ -> false })

                    .create()
                    .show()
        }
//


//            NiceShowView.getInstance().NormalToast(" " + testa().getresult(50)).show()
//            ApiEntry.getInstance().requestPermission(this, arrayOf(Manifest.permission.READ_PHONE_STATE))



//        NiceAlertDialog.Builder(supportFragmentManager.beginTransaction())
//                 .setBackgroundRes(R.drawable.bg_normal_dialog_view)
//                 .setLayoutRes(R.layout.view_progress)
//                 .setCancelable(true)
//                 .setTag("newDialog")
//                 .setScreenHeightPercent(this, 0.15f)
//                 .setScreenWidthPercent(this, 0.25f)
//                 .setAnimationRes(R.style.NiceDialogAnim)
//                 .setGravity(Gravity.CENTER)
//                 .setDimAmount(0.0f)
//                 .setBindViewListener(object : NiceAlertDialog.OnBindViewListener {
//                     override fun onBindView(viewHolder: NiceAdapter.VH) {
//                         viewHolder.getView<NiceCircleProgress>(R.id.np_loading)
////                            viewHolder.getView<NiceTextView>(R.id.btv_dialog_confirm).text ="确认"
////                            viewHolder.getView<NiceTextView>(R.id.btv_dialog_cancel).text ="取消"
////                            viewHolder.getView<NiceTextView>(R.id.btv_dialog_message).text ="这是个测试的dialog"
//                     }
//                 })
////                    .addClickedId(R.id.btv_dialog_confirm, R.id.btv_dialog_cancel, R.id.btv_dialog_message)
//                 .create().show()
//        }

/*
        val appInfos = AppUtils.getInstance().getAppsInfo(false, this)

        test_btn.setOnClickListener {
            NiceAlertDialog.Builder(supportFragmentManager.beginTransaction())
                    .setLayoutRes(R.layout.list_dialog_test)
                    .setCancelable(false)
                    .setTag("newDialog")
                    .setScreenHeightPercent(this, 0.2f)
                    .setScreenWidthPercent(this, 0.2f)
                    .setAnimationRes(R.style.NiceDialogAnim)
                    .setGravity(Gravity.BOTTOM)
                    .setDimAmount(0.0f)
                    .setListRes(R.id.recycler_test, LinearLayoutManager.VERTICAL)
                    .setAdapter(object :NiceAdapter<AppInfo>(appInfos){
                        override fun getLayout(viewType: Int): Int {
                            return R.layout.app_info_view
                        }

                        override fun convert(holder: VH, item: AppInfo, position: Int, payloads: MutableListA<ny>?) {
                            holder.getView<TextView>(R.id.tv_app_name).text = item.appName
                            holder.getView<TextView>(R.id.tv_package_name).text = item.packageName
//                            holder.getView<TextView>(R.id.tv_app_size).text = item.signMd5
                           }

                    })
                    .setListItemClickListener(object :NiceAdapter.ItemClickListener{
                        override fun setItemClick(holder: NiceAdapter.VH, position: Int) {
                            NiceShowView.getInstance().NormalToast(appInfos[position].appName).show()
                        }

                        override fun setItemLongClick(holder: NiceAdapter.VH, position: Int): Boolean {
                            NiceShowView.getInstance().NormalToast(appInfos[position].signMd5).show()
                            return true
                        }

                    })
                    .create()
                    .show()
*/

//            niceDialog = NiceAlertDialog.Builder(supportFragmentManager.beginTransaction())
//                    .setBackgroundRes(R.drawable.bg_normal_dialog_view)
//                    .setLayoutRes(R.layout.view_photo_dialog)
//                    .setCancelable(false)
//                    .setTag("newDialog")
//                    .setScreenHeightPercent(this, 0.2f)
//                    .setScreenWidthPercent(this, 1.0f)
//                    .setAnimationRes(R.style.NiceDialogAnim)
//                    .setGravity(Gravity.BOTTOM)
//                    .setDimAmount(0.0f)
//                    .setBindViewListener(object :NiceAlertDialog.OnBindViewListener{
//                        override fun onBindView(viewHolder: NiceAdapter.VH) {
//                            viewHolder.getView<NiceTextView>(R.id.ntv_photo_dialog_camera).text ="相机"
//                            viewHolder.getView<NiceTextView>(R.id.ntv_photo_dialog_photo).text ="照片"
//                            viewHolder.getView<NiceTextView>(R.id.ntv_photo_dialog_cancel).text ="取消"
//
//                        }
//                    })
//                    .addClickedId(R.id.ntv_photo_dialog_camera, R.id.ntv_photo_dialog_photo, R.id.ntv_photo_dialog_cancel)
//                    .setViewClickListener(object :NiceAlertDialog.OnViewClickListener{
//                        override fun onClick(viewHolder: NiceAdapter.VH, view: View, dialog: NiceAlertDialog) {
//                            when(view.id){
//                                R.id.ntv_photo_dialog_camera -> NiceShowView.getInstance().NormalToast("相机").show()
//                                R.id.ntv_photo_dialog_photo -> NiceShowView.getInstance().NormalToast("照片").show()
//                                R.id.ntv_photo_dialog_cancel -> niceDialog?.dismiss()
//                            }
//                        }
//                    })
//                    .setKeyListener(object : DialogInterface.OnKeyListener{
//                        override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
//                            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                                NiceShowView.getInstance().NormalToast("back failed").show()
//                                return true
//                            }
//                            return  false
//                        }
//                    })
//
//                    .create()
//                    .show()


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

    private fun showDialog(message:String){
        NiceShowView.getInstance().createDialog(this@MainActivity, NiceDialog.DIALOG_NORMAL)
        .setTitle("关于权限")
                .setMessage(message)
                .setCanceled(true)
                .setCancel("取消", object : NiceDialog.DialogClickListener {
                    override fun onClick() {

                    }
                })
                .setConfirm("去设置", object : NiceDialog.DialogClickListener {
                    override fun onClick() {
                        NicePermissions.startActivityToSetting(this@MainActivity)
                    }
                }).show()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

    }

    private fun setpermission() {

    }

    override fun onInit() {

    }

    override fun onStart() {
        super.onStart()

//        NicePermissionUtils.getInstance().requestPermissions()
//        ApiEntry.getInstance().requestPermission(this)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
//        Assist.unRegisterNetReceiver()
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
        mPhotoUtils?.onActivityResult(requestCode, resultCode, data)
        LogUtils.getInstance().d("request:" + requestCode)
        LogUtils.getInstance().d("result:" + resultCode)
        LogUtils.getInstance().d("data:" + data?.data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        ApiEntry.getInstance().handleRequestPermissionsResult(requestCode, permissions, grantResults)
//        NicePermissionUtils.getInstance().handleRequestPermissionsResult( requestCode, permissions, grantResults)
    }

    override fun isBackTwice(): Boolean {
        return true
    }
//    override fun permissionGrantedCallback(permissions: MutableList< String>) {
//        val st = StringBuilder("Granted\n")
//        permissions.forEach {
//            st.append(it)
//        }
//        LogUtils.getInstance().e(st.toString(), "pipa")
//        if(Manifest.permission.CAMERA in permissions) {
//            val file = File(Environment.getExternalStorageDirectory(), "pic_" + System.currentTimeMillis() / 1000 + ".jpg")
//            val uri = if (AppUtils.getInstance().getApiLevel() > Build.VERSION_CODES.N) {
//                FileProvider.getUriForFile(this, AppUtils.getInstance().getPackageName() + ".provider", file)
//            } else {
//                Uri.fromFile(file)
//            }
//            PhotoUtils.openCamera(this, uri, 1)
//        }
//    }

//    override fun permissionDeniedCallback(permissions: MutableList< String>) {
//        val st = StringBuilder("Denied\n")
//        permissions.forEach {
//            st.append(it)
//        }
//        LogUtils.getInstance().e(st.toString(), "pipa")
//    }
//
//    override fun permissionIgnoreCallback(permissions: MutableList< String>) {
//        val st = StringBuilder("Ignore\n")
//        permissions.forEach {
//            st.append(it)
//        }
//        LogUtils.getInstance().e(st.toString(), "pipa")
//    }

}

