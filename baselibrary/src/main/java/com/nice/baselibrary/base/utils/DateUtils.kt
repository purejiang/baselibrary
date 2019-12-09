package com.nice.baselibrary.base.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期工具类
 * @author JPlus
 * @date 2019/6/11.
 */
class DateUtils {
    companion object {
        /**
         * 获取当前时间
         * @
         * @return yyyy-MM-dd_HH:mm:ss格式的时间
         */
        @SuppressLint("SimpleDateFormat")
        fun getDateTimeByMillis(isIncludeBlank:Boolean): String {
            return if(isIncludeBlank) {
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(System.currentTimeMillis()))
            }else{
                SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Date(System.currentTimeMillis()))
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun getStartDateTime(isMonth:Boolean, date:Date):String{
            return if(isMonth){
                SimpleDateFormat("yyyy-MM-01 00:00:00").format(date)
            }else{
                SimpleDateFormat("yyyy-01-01 00:00:00").format(date)
            }

        }
        @SuppressLint("SimpleDateFormat")
        fun getEndDateTime(isMonth:Boolean, date:Date):String{
            return if(isMonth){
                val cal = Calendar.getInstance()
                SimpleDateFormat("yyyy-MM-${cal.getActualMaximum(Calendar.DAY_OF_MONTH)} 23:59:59").format(cal.time)
            }else{
                SimpleDateFormat("yyyy-12-31 23:59:59").format(date)
            }

        }
    }


}