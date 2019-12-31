package com.nice.baselibrary.base.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nice.baselibrary.base.utils.ActivityCollect
import com.nice.baselibrary.base.utils.LogUtils
import com.nice.baselibrary.base.utils.showNormalToast

/**
 * @author JPlus
 * @date 2019/1/16.
 */
abstract class BaseActivity : AppCompatActivity() {
    companion object {
        private const val BACK_TIME = 2000
    }

    private var mBackTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.d("Activity:[${this.localClassName}] --onCreate()")

        ActivityCollect.add(this)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        LogUtils.d("Activity:[${this.localClassName}] --setContentView()")
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        LogUtils.d("Activity:[${this.localClassName}] --onSaveInstanceState()")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LogUtils.d("Activity:[${this.localClassName}] --onConfigurationChanged()")
    }

    override fun onStart() {
        super.onStart()
        LogUtils.d("Activity:[${this.localClassName}] --onStart()")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.d("Activity:[${this.localClassName}] --onPause()")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.d("Activity:[${this.localClassName}] --onStop()")
    }

    override fun onRestart() {
        super.onRestart()
        LogUtils.d("Activity:[${this.localClassName}] --onRestart()")
    }

    override fun onResume() {
        super.onResume()
        LogUtils.d("Activity:[${this.localClassName}] --onResume()")
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollect.remove(this)
        LogUtils.d("Activity:[${this.localClassName}]--onDestroy()")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        LogUtils.d("Activity:[${this.localClassName}] --onNewIntent()")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.d("Activity:[${this.localClassName}] --onActivityResult()")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        LogUtils.d("Activity:[${this.localClassName}] --onRequestPermissionsResult()")
    }

    /**
     * 是否点击两次退出
     * @return 返回空则无两次点击退出
     */
    open fun setBackTwiceMsg(): String? {
        return null
    }

    override fun onBackPressed() {
        setBackTwiceMsg()?.let {
            if (System.currentTimeMillis() - mBackTime > BACK_TIME) {
                mBackTime = System.currentTimeMillis()
                this.showNormalToast(it)
            } else {
                ActivityCollect.removeAll()
            }
            return
        }
        super.onBackPressed()
    }

}