package com.wuruoye.all2.base.util

import java.util.*

/**
 * Created by wuruoye on 2017/9/21.
 * this file is to do
 */
object DateUtil {

    fun getDateString(time: Long): String{
        val calender = Calendar.getInstance()
        calender.timeInMillis = time

        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH) + 1
        val day = calender.get(Calendar.DAY_OF_MONTH)
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val minute = calender.get(Calendar.MINUTE)

        return "$year.$month.$day $hour:$minute"
    }
}