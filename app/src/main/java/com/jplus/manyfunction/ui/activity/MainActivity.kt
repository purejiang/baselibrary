package com.jplus.manyfunction.ui.activity


import android.content.Intent
import android.os.Bundle
import com.jplus.manyfunction.R
import com.jplus.manyfunction.utils.NicePermissionUtils
import com.leniu.assist.ln.entity.AppInfo
import com.nice.baselibrary.base.NiceActivity
import com.nice.baselibrary.base.common.ApiEntry
import com.nice.baselibrary.base.utils.AppUtils
import com.nice.baselibrary.base.utils.LogUtils
import com.nice.baselibrary.base.utils.SharePreferenceUtils
import com.nice.baselibrary.base.view.NiceTitleBar

class MainActivity : NiceActivity(){
    private var mTitleBar: NiceTitleBar?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ApiEntry.getInstance().init(this, true)
        val mSpUtils = SharePreferenceUtils.getInstance().create(this)
        val appInfoList = AppUtils.getInstance().getAppsInfo( false)
        val sb = StringBuilder("appInfos:")
        NicePermissionUtils.getInstance().init(this, true, false)
        NicePermissionUtils.getInstance().requestPermissions()
//        for (info:AppInfo in appInfoList){
//            sb.append(info.appName+":"+info.signMd5).append("\n")
//        }
//        LogUtils.getInstance().d(sb.toString())
//        if(!mSpUtils.contains("notFirstOpen")){
//           mSpUtils.set("notFirstOpen", true)
//        }else{
//            startActivity(Intent(this, DownloadListActivity::class.java))
//            finish()
//        }
    }

    override fun onInit() {

    }

    override fun onStart() {
        super.onStart()
//        ApiEntry.getInstance().requestPermission()
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
//        ApiEntry.getInstance().handleRequestPermissionsResult(requestCode, permissions, grantResults)
        NicePermissionUtils.getInstance().handleRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
