package com.nice.baselibrary.base.utils

import com.nice.baselibrary.base.ui.NiceActivity
import java.util.*

/**
 * Activity管理类
 * @author JPlus
 * @date 2019/1/16.
 */
class ActivityCollect {
    companion object {
        private val M_ACTIVITY_LIST: MutableList<NiceActivity> = ArrayList()
        /**
         * 添加Activity
         * @param activity
         */
        fun add(activity: NiceActivity) {
            M_ACTIVITY_LIST.add(activity)
        }

        /**
         * 移除Activity
         * @param activity
         */
        fun remove(activity: NiceActivity) {
            M_ACTIVITY_LIST.remove(activity)
        }
        /**
         * 销毁所有Activity
         */
        fun removeAll() {
            M_ACTIVITY_LIST
                    .filterNot { it.isFinishing }
                    .forEach { it.finish() }
        }
    }
}


