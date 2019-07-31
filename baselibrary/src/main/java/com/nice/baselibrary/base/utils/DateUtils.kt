package com.nice.baselibrary.base.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Jplus on 2019/6/11.
 */
class DateUtils {
    companion object {
        fun getMonthEndDay(year:Int, monthOfYear:Int):String{
            val cal = Calendar.getInstance()
            // 不加下面2行，就是取当前时间前一个月的第一天及最后一天
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear-1)
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
            cal.set(Calendar.HOUR_OF_DAY, 23)
            cal.set(Calendar.MINUTE, 59)
            cal.set(Calendar.SECOND, 59)
            return getEndDateTime(true, cal.time)
        }

        /**
         * 获取当前时间
         * @
         * @return yyyy-MM-dd_HH:mm:ss格式的时间
         */
        fun getDateTimeByMillis(isIncludeBlank:Boolean): String {

            return if(isIncludeBlank) {
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(System.currentTimeMillis()))
            }else{
                SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Date(System.currentTimeMillis()))
            }
        }

        fun getStartDateTime(isMonth:Boolean, date:Date):String{
            return if(isMonth){
                SimpleDateFormat("yyyy-MM-01 00:00:00").format(date)
            }else{
                SimpleDateFormat("yyyy-MM-dd 00:00:00").format(date)
            }

        }
        fun getEndDateTime(isMonth:Boolean, date:Date):String{
            return if(isMonth){
                SimpleDateFormat("yyyy-MM-dd 23:59:59").format(date)
            }else{
                SimpleDateFormat("yyyy-MM-dd 23:59:59").format(date)
            }

        }
    }


}