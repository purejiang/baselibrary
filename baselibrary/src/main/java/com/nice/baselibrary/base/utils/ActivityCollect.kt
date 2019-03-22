package com.nice.baselibrary.base.utils

import com.nice.baselibrary.base.BaseActivity
import java.util.*

/**
 * Activity管理类
 * @author JPlus
 * @date 2019/1/16.
 */
class ActivityCollect {
    companion object {
        private val mActivityList: MutableList<BaseActivity> = ArrayList()
        /**
         * 添加Activity
         * @param activity
         */
        fun add(activity: BaseActivity) {
            mActivityList.add(activity)
        }

        /**
         * 移除Activity
         * @param activity
         */
        fun remove(activity: BaseActivity) {
            mActivityList.remove(activity)
        }
        /**
         * 销毁所有Activity
         */
        fun removeAll() {
            mActivityList
                    .filterNot { it.isFinishing }
                    .forEach { it.finish() }
        }
    }
}


