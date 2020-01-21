package com.nice.baselibrary.base.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期工具方法
 * @author JPlus
 * @date 2019/6/11.
 */

/**
 * 获取当前时间
 * @param isIncludeBlank 是否包含空格
 * @return yyyy-MM-dd_HH:mm:ss格式的时间
 */
@SuppressLint("SimpleDateFormat")
fun Date.getDateTimeByMillis(isIncludeBlank: Boolean): String {
    return if (isIncludeBlank) {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this)
    } else {
        SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(this)
    }
}
/**
 * 获取开始时间
 * @param isMonth true 返回本月最初时间，false则返回本年最初时间
 * @return yyyy-MM-dd HH:mm:ss格式的时间
 */
@SuppressLint("SimpleDateFormat")
fun Date.getStartDateTime(isMonth: Boolean): String {
    return if (isMonth) {
        SimpleDateFormat("yyyy-MM-01 00:00:00").format(this)
    } else {
        SimpleDateFormat("yyyy-01-01 00:00:00").format(this)
    }

}
/**
 * 获取最后时间
 * @param isMonth true 返回本月最后时间，false则返回本年最后时间
 * @return yyyy-MM-dd HH:mm:ss格式的时间
 */
@SuppressLint("SimpleDateFormat")
fun Date.getEndDateTime(isMonth: Boolean): String {
    return if (isMonth) {
        val cal = Calendar.getInstance()
        SimpleDateFormat("yyyy-MM-${cal.getActualMaximum(Calendar.DAY_OF_MONTH)} 23:59:59").format(cal.time)
    } else {
        SimpleDateFormat("yyyy-12-31 23:59:59").format(date)
    }

}

