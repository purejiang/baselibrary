package com.jplus.manyfunction

import android.content.Context
import com.nice.baselibrary.base.common.ApiEntry
import com.nice.baselibrary.base.ui.NiceApplication
import com.squareup.leakcanary.LeakCanary


/**
 * @author Administrator
 * @date 2019/2/20.
 */
class MyApplication: NiceApplication() {

    override fun onCreate() {
        super.onCreate()
//        LeakCanary.install(this)
        ApiEntry.getInstance().init(this, true)
    }

    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }

}