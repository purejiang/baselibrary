package com.jplus.manyfunction.ui.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.jplus.manyfunction.R
import com.jplus.manyfunction.utils.NicePermissionUtils
import com.leniu.assist.ln.entity.AppInfo
import com.nice.baselibrary.base.NiceActivity
import com.nice.baselibrary.base.common.ApiEntry
import com.nice.baselibrary.base.common.Constant
import com.nice.baselibrary.base.utils.*
import com.nice.baselibrary.base.view.NiceTitleBar
import java.io.File
import java.sql.DatabaseMetaData
import java.sql.DriverManager
import android.database.sqlite.SQLiteDatabase
import com.nice.baselibrary.base.view.NiceDialog
import com.nice.baselibrary.base.view.NiceShowView
import java.util.*


class MainActivity : NiceActivity(){
    private var mTitleBar: NiceTitleBar?=null
    private var niceDialog:NiceDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        niceDialog = NiceShowView.getInstance().createDialog(this, NiceDialog.DIALOG_SMALLER)
//                .setTitle("加载")
                .setCanceled(false)
                .setCircleProgress("loading...")
        niceDialog?.show()

        ApiEntry.getInstance().init(this, true)
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                niceDialog?.dismiss()
            }
        },3000)
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
        ApiEntry.getInstance().destory()
//        NicePermissionUtils.getInstance().destroy()
    }
    override fun onFindView() {
        mTitleBar = findViewById(R.id.base_tb_main)
        mTitleBar?.setTitleBarColor(R.color.colorAccent)
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
        ApiEntry.getInstance().handleRequestPermissionsResult(requestCode, permissions, grantResults)
//        NicePermissionUtils.getInstance().handleRequestPermissionsResult( requestCode, permissions, grantResults)
    }
}
