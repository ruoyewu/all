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

        val newMonth =
                if (month < 10){
                    "0$month"
                }else{
                    month.toString()
                }

        val newDay =
                if (day < 10){
                    "0$day"
                }else{
                    day.toString()
                }

        val newHour =
                if (hour < 10){
                    "0$hour"
                }else{
                    hour.toString()
                }

        val newMinute =
                if (minute < 10){
                    "0$minute"
                }else{
                    minute.toString()
                }

        return "$year.$newMonth.$newDay\t$newHour:$newMinute"
    }

    fun getTime(time: Long): String{
        val second = time / 1000
        val minute = second / 60
        val hour = minute / 60
        val day = hour / 24
        val year = day / 365
        return when {
            year > 0 -> "$year 年 $day 天 $hour 小时"
            day > 0 -> "$day 天 $hour 小时 $minute 分钟"
            hour > 0 -> "$hour 小时 $minute 分钟 $second 秒"
            minute > 0 -> "$minute 分钟 $second 秒"
            else -> "$second 秒"
        }
    }

    fun getWeek(): Int{
        val calender = Calendar.getInstance()
        return calender.get(Calendar.DAY_OF_WEEK)
    }
}