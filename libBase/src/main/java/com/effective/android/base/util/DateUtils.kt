package com.effective.android.base.util

import android.util.Log

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

/**
 * data utils
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

object DateUtils {

    private val TAG = DateUtils::class.java.simpleName

    private val TIME_MILLISECONDS = 1000
    private val TIME_NUMBERS = 60
    private val TIME_HOURSES = 24
    private val FORMAT = "yyyy-MM-dd HH:mm:ss"

    @JvmStatic
    val timeZone: TimeZone
        get() = TimeZone.getDefault()

    @JvmStatic
    val gmtUnixTimeByCalendar: Long
        get() {
            val calendar = Calendar.getInstance()
            val unixTime = calendar.timeInMillis
            return unixTime - TimeZone.getDefault().rawOffset
        }

    @JvmStatic
    val unixTimeByCalendar: Long
        get() {
            val calendar = Calendar.getInstance()
            return calendar.timeInMillis
        }

    /**
     * @param date 日期字符串，必须为"yyyy-MM-dd HH:mm:ss"
     * @param date
     * @return
     */
    @JvmStatic
    fun parseDate(date: String): Date? {
        return parseDate(date, FORMAT)
    }

    @JvmStatic
    fun parseDate(date: String, format: String): Date? {
        var dt: Date? = null
        val dateFormat = SimpleDateFormat(format)
        try {
            dt = dateFormat.parse(date)
        } catch (e: ParseException) {
            Log.e(TAG, e.message)
        }

        return dt
    }


    @JvmStatic
    fun formatDate(date: Date, format: String = FORMAT): String {
        val dateFormat = SimpleDateFormat(format)
        return dateFormat.format(date)
    }

    @JvmStatic
    fun formatUnixTime(unixTime: Long, format: String = FORMAT): String {
        val dateFormat = SimpleDateFormat(format)
        return dateFormat.format(unixTime)
    }

    @JvmOverloads
    fun formatGMTUnixTime(gmtUnixTime: Long, format: String = FORMAT): String {
        val dateFormat = SimpleDateFormat(format)
        return dateFormat.format(gmtUnixTime + TimeZone.getDefault().rawOffset)
    }

    @JvmStatic
    fun getDate(unixTime: Long): Date {
        return Date(unixTime)
    }

    @JvmStatic
    fun getGMTDate(gmtUnixTime: Long): Date {
        return Date(gmtUnixTime + TimeZone.getDefault().rawOffset)
    }

    @JvmStatic
    fun getGMTUnixTime(unixTime: Long): Long {
        return unixTime - TimeZone.getDefault().rawOffset
    }

    /**
     * 将GMT Unix时间戳转换为系统默认时区的Unix时间戳
     *
     * @param gmtUnixTime
     * @return
     */
    @JvmStatic
    fun getCurrentTimeZoneUnixTime(gmtUnixTime: Long): Long {
        return gmtUnixTime + TimeZone.getDefault().rawOffset
    }

    @JvmStatic
    fun changeTimeZone(date: Date?, oldZone: TimeZone, newZone: TimeZone): Date? {
        var dateTmp: Date? = null
        if (date != null) {
            val timeOffset = oldZone.rawOffset - newZone.rawOffset
            dateTmp = Date(date.time - timeOffset)
        }
        return dateTmp
    }

    @JvmStatic
    fun formatTime(seconds: Long): String {
        val hh = seconds / TIME_NUMBERS.toLong() / TIME_NUMBERS.toLong()
        val mm = if (seconds - hh * TIME_NUMBERS.toLong() * TIME_NUMBERS.toLong() > 0) (seconds - hh * TIME_NUMBERS.toLong() * TIME_NUMBERS.toLong()) / TIME_NUMBERS else 0
        val ss = if (seconds < TIME_NUMBERS) seconds else seconds % TIME_NUMBERS
        return ((if (hh == 0L) "" else (if (hh < 10) "0$hh" else hh).toString() + "小时")
                + (if (mm == 0L) "" else (if (mm < 10) "0$mm" else mm).toString() + "分")
                + if (ss == 0L) "" else (if (ss < 10) "0$ss" else ss).toString() + "秒")
    }

    @JvmStatic
    fun getDiffTime(date: Long): String {
        var strTime = "很久很久以前"
        val time = Math.abs(Date().time - date)
        // 一分钟以内
        if (time < TIME_NUMBERS * TIME_MILLISECONDS) {
            strTime = "刚刚"
        } else {
            val min = (time / TIME_MILLISECONDS.toLong() / TIME_NUMBERS.toLong()).toInt()
            if (min < TIME_NUMBERS) {
                if (min < 15) {
                    strTime = "一刻钟前"
                } else if (min < 30) {
                    strTime = "半小时前"
                } else {
                    strTime = "1小时前"
                }
            } else {
                val hh = min / TIME_NUMBERS
                if (hh < TIME_HOURSES) {
                    strTime = hh.toString() + "小时前"
                } else {
                    val days = hh / TIME_HOURSES
                    if (days <= 6) {
                        strTime = days.toString() + "天前"
                    } else {
                        val weeks = days / 7
                        if (weeks < 3) {
                            strTime = weeks.toString() + "周前"
                        }
                    }
                }
            }
        }
        return strTime
    }

    @JvmStatic
    fun daysBetween(date1: Date, date2: Date): Int {
        val cal1 = Calendar.getInstance()
        cal1.time = date1

        val cal2 = Calendar.getInstance()
        cal2.time = date2
        val day1 = cal1.get(Calendar.DAY_OF_YEAR)
        val day2 = cal2.get(Calendar.DAY_OF_YEAR)

        val year1 = cal1.get(Calendar.YEAR)
        val year2 = cal2.get(Calendar.YEAR)
        //同一年
        if (year1 != year2) {
            var timeDistance = 0
            for (i in year1 until year2) {
                //闰年
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366
                } else {
                    timeDistance += 365
                }//不是闰年
            }
            return timeDistance + (day2 - day1)
        } else {
            return day2 - day1
        }//不同年
    }

    @JvmStatic
    fun daysBetweenInSecond(date1: Date, date2: Date): Double {
        val beginDateLong = date1.time    // Date型转换成Long型毫秒数，用于计算
        val endDateLong = date2.time
        val durationLong = endDateLong - beginDateLong
        val totalSeconds = (durationLong / 1000).toDouble()// 总共的秒数
        val secondsOfDay = (24 * 60 * 60).toDouble()// 一天的秒数
        return totalSeconds / secondsOfDay
    }

    /**
     * 判断时间是否在时间段内
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    @JvmStatic
    fun belongTime(nowTime: Date, beginTime: Date, endTime: Date): Boolean {
        val date = Calendar.getInstance()
        date.time = nowTime

        val begin = Calendar.getInstance()
        begin.time = beginTime

        val end = Calendar.getInstance()
        end.time = endTime

        return date.after(begin) && date.before(end)
    }

}

