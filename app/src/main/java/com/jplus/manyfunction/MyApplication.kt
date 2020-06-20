package com.jplus.manyfunction

import com.nice.baselibrary.base.common.BaseLibrary
import com.nice.baselibrary.base.common.Constant
import com.nice.baselibrary.base.ui.BaseApplication
import java.io.File


/**
 * @author JPlus
 * @date 2019/2/20.
 */
class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        BaseLibrary.instance.initUtils(this, true, File(Constant.Path.ROOT_DIR, Constant.Path.PATCH_DEX_DIR))

    }
}