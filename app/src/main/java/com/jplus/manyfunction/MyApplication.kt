package com.jplus.manyfunction

import com.nice.baselibrary.base.common.BaseLibrary
import com.nice.baselibrary.base.common.Constant
import com.nice.baselibrary.base.net.RequestTool
import com.nice.baselibrary.base.ui.BaseApplication
import com.squareup.leakcanary.LeakCanary
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.PATCH
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * @author JPlus
 * @date 2019/2/20.
 */
class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
        BaseLibrary.instance.initUtils(this, true, File(Constant.Path.ROOT_DIR, Constant.Path.PATCH_DEX_DIR))
    }
}