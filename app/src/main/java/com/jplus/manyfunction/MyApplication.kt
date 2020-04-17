package com.jplus.manyfunction

import com.nice.baselibrary.base.common.BaseLibrary
import com.nice.baselibrary.base.common.Constant
import com.nice.baselibrary.base.ui.BaseApplication
import com.nice.baselibrary.widget.loading.LoadingManager
import com.squareup.leakcanary.LeakCanary
import java.io.File


/**
 * @author JPlus
 * @date 2019/2/20.
 */
class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
        BaseLibrary.instance.initUtils(this, true, File(Constant.Path.ROOT_DIR, Constant.Path.PATCH_DEX_DIR))
        LoadingManager.DEFAULT_RETRY_RES = R.layout.layout_test_retry
        LoadingManager.DEFAULT_LOADING_RES = R.layout.layout_test_loading
        LoadingManager.DEFAULT_EMPTY_RES = R.layout.layout_test_empty
    }
}