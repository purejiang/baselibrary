package com.jplus.manyfunction.ui.activity


import android.os.Bundle
import com.jplus.manyfunction.R
import com.nice.baselibrary.base.BaseActivity
import com.nice.baselibrary.base.common.ApiEntry
import com.nice.baselibrary.base.view.NiceTitleBar

class MainActivity : BaseActivity() {
    private var mTitleBar: NiceTitleBar?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ApiEntry.getInstance().init(this, true)
    }

    override fun onInit() {

    }

    override fun onStart() {
        super.onStart()
        ApiEntry.getInstance().requestPermission()
    }
    override fun onFindView() {
        mTitleBar = findViewById(R.id.base_tb_main)
        mTitleBar?.setTitleBarColor(R.color.colorAccent)
    }

    override fun onBindListener() {

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        ApiEntry.getInstance().handleRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
