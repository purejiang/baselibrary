package com.jplus.manyfunction

import android.content.Context
import com.nice.baselibrary.base.common.BaseLibrary
import com.nice.baselibrary.base.ui.BaseApplication
import com.squareup.leakcanary.LeakCanary


/**
 * @author JPlus
 * @date 2019/2/20.
 */
class MyApplication: BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
//        InitializeManager.instance.initApplication(this)
        BaseLibrary.instance.init(this)
        BaseLibrary.instance.initUtils(true)
    }

    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }


}