package com.nice.baselibrary.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import com.nice.baselibrary.base.utils.ActivityCollect
import com.nice.baselibrary.base.utils.LogUtils

/**
 * @author JPlus
 * @date 2019/1/16.
 */
abstract class NiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollect.add(this)
        LogUtils.getInstance().d("onCreate()")
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        onSetContent()
    }

    private fun onSetContent() {
        onInit()
        onFindView()
        onBindListener()
    }

    /**
     * 初始化操作
     */
    abstract fun onInit()

    /**
     * 绑定id
     */
    abstract fun onFindView()

    /**
     * 绑定监听
     */
    abstract fun onBindListener()

    override fun onStart() {
        super.onStart()
        LogUtils.getInstance().d("onStart()")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.getInstance().d("onPause()")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.getInstance().d("onStop()")
    }

    override fun onRestart() {
        super.onRestart()
        LogUtils.getInstance().d("onRestart()")
    }
    override fun onResume() {
        super.onResume()
        LogUtils.getInstance().d("onResume()")
    }
    override fun onDestroy() {
        super.onDestroy()
        ActivityCollect.remove(this)
        LogUtils.getInstance().d("onDestroy()")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        LogUtils.getInstance().d("onNewIntent()")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.getInstance().d("onActivityResult()")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        LogUtils.getInstance().d("onRequestPermissionsResult()")
    }



}