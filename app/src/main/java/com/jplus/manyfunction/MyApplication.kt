package com.jplus.manyfunction

import com.jplus.manyfunction.download.JDownloadManager
import com.nice.baselibrary.base.common.BaseLibrary
import com.nice.baselibrary.base.ui.BaseApplication
import com.squareup.leakcanary.LeakCanary


/**
 * @author JPlus
 * @date 2019/2/20.
 */
class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
        BaseLibrary.instance.initUtils(this, true)

    }

}