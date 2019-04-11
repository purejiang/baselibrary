package com.jplus.manyfunction

import android.content.Context
import com.nice.baselibrary.base.NiceApplication
import com.squareup.leakcanary.LeakCanary


/**
 * @author Administrator
 * @date 2019/2/20.
 */
class MyApplication: NiceApplication() {

    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
    }

    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }

}