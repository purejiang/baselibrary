package com.nice.baselibrary.base.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import com.nice.baselibrary.base.ui.view.NiceShowView
import com.nice.baselibrary.base.utils.ActivityCollect
import com.nice.baselibrary.base.utils.LogUtils

/**
 * @author JPlus
 * @date 2019/1/16.
 */
abstract class NiceActivity : AppCompatActivity() {
    companion object {
        private val BACK_TIME =2000
    }

    private var mBackTime=0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollect.add(this)
        LogUtils.getInstance().d(this.localClassName + " --onCreate()")
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

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        LogUtils.getInstance().d(this.localClassName + " --onConfigurationChanged()")
    }

    override fun onStart() {
        super.onStart()
        LogUtils.getInstance().d(this.localClassName + " --onStart()")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.getInstance().d(this.localClassName + " --onPause()")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.getInstance().d(this.localClassName + " --onStop()")
    }

    override fun onRestart() {
        super.onRestart()
        LogUtils.getInstance().d(this.localClassName + " --onRestart()")
    }

    override fun onResume() {
        super.onResume()
        LogUtils.getInstance().d(this.localClassName + " --onResume()")
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollect.remove(this)
        LogUtils.getInstance().d(this.localClassName + " --onDestroy()")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        LogUtils.getInstance().d(this.localClassName + " --onNewIntent()")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.getInstance().d(this.localClassName + " --onActivityResult()")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        LogUtils.getInstance().d(this.localClassName + " --onRequestPermissionsResult()")
    }

    /**
     * 是否点击两次退出
     * @return
     */
    open fun isBackTwice():Boolean{
        return false
    }

    override fun onBackPressed() {
        if(isBackTwice()){
            if(System.currentTimeMillis()- mBackTime>BACK_TIME){
                mBackTime = System.currentTimeMillis()
                NiceShowView.getInstance().NormalToast("再按一次退出~").show()
            }else{
                ActivityCollect.removeAll()
                System.exit(0)
            }
        }else {
            super.onBackPressed()
        }
    }

}