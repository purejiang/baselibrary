package com.nice.baselibrary.base.ui

import android.app.Application
import android.content.Context

/**
 * @author JPlus
 * @date 2019/2/13.
 */
abstract class BaseApplication :Application() {
    override fun onCreate() {
        super.onCreate()
    }
}