package com.nice.baselibrary.base.utils

import android.app.Activity
import com.nice.baselibrary.base.ui.BaseActivity
import java.util.*
import kotlin.system.exitProcess

/**
 * Activity管理类
 * @author JPlus
 * @date 2019/1/16.
 */
class ActivityCollect {
    companion object {
        private val M_ACTIVITY_LIST: MutableList<Activity> = ArrayList()
        /**
         * 添加Activity
         * @param activity
         */
        fun add(activity: Activity) {
            M_ACTIVITY_LIST.add(activity)
        }

        /**
         * 移除Activity
         * @param activity
         */
        fun remove(activity: Activity) {
            M_ACTIVITY_LIST.remove(activity)
        }
        /**
         * 销毁所有Activity
         */
        fun removeAll() {
            LogUtils.instance.saveLog()
            M_ACTIVITY_LIST.filterNot { it.isFinishing }
                    .forEach { it.finish() }
        }
    }
}


