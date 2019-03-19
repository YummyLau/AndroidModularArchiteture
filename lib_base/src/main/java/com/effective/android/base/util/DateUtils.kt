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

    val timeZone: TimeZone
        get() = TimeZone.getDefault()

    val gmtUnixTimeByCalendar: Long
        get() {
            val calendar = Calendar.getInstance()
            val unixTime = calendar.timeInMillis
            return unixTime - TimeZone.getDefault().rawOffset
        }

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
    fun parseDate(date: String): Date? {
        return parseDate(date, FORMAT)
    }

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


    @JvmOverloads
    fun formatDate(date: Date, format: String = FORMAT): String {
        val dateFormat = SimpleDateFormat(format)
        return dateFormat.format(date)
    }

    @JvmOverloads
    fun formatUnixTime(unixTime: Long, format: String = FORMAT): String {
        val dateFormat = SimpleDateFormat(format)
        return dateFormat.format(unixTime)
    }

    @JvmOverloads
    fun formatGMTUnixTime(gmtUnixTime: Long, format: String = FORMAT): String {
        val dateFormat = SimpleDateFormat(format)
        return dateFormat.format(gmtUnixTime + TimeZone.getDefault().rawOffset)
    }

    fun getDate(unixTime: Long): Date {
        return Date(unixTime)
    }

    fun getGMTDate(gmtUnixTime: Long): Date {
        return Date(gmtUnixTime + TimeZone.getDefault().rawOffset)
    }

    fun getGMTUnixTime(unixTime: Long): Long {
        return unixTime - TimeZone.getDefault().rawOffset
    }

    /**
     * 将GMT Unix时间戳转换为系统默认时区的Unix时间戳
     *
     * @param gmtUnixTime
     * @return
     */
    fun getCurrentTimeZoneUnixTime(gmtUnixTime: Long): Long {
        return gmtUnixTime + TimeZone.getDefault().rawOffset
    }

    fun changeTimeZone(date: Date?, oldZone: TimeZone, newZone: TimeZone): Date? {
        var dateTmp: Date? = null
        if (date != null) {
            val timeOffset = oldZone.rawOffset - newZone.rawOffset
            dateTmp = Date(date.time - timeOffset)
        }
        return dateTmp
    }

    fun formatTime(seconds: Long): String {
        val hh = seconds / TIME_NUMBERS.toLong() / TIME_NUMBERS.toLong()
        val mm = if (seconds - hh * TIME_NUMBERS.toLong() * TIME_NUMBERS.toLong() > 0) (seconds - hh * TIME_NUMBERS.toLong() * TIME_NUMBERS.toLong()) / TIME_NUMBERS else 0
        val ss = if (seconds < TIME_NUMBERS) seconds else seconds % TIME_NUMBERS
        return ((if (hh == 0L) "" else (if (hh < 10) "0$hh" else hh).toString() + "小时")
                + (if (mm == 0L) "" else (if (mm < 10) "0$mm" else mm).toString() + "分")
                + if (ss == 0L) "" else (if (ss < 10) "0$ss" else ss).toString() + "秒")
    }

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

}
/**
 * @param date 日期字符串，必须为"yyyy-MM-dd HH:mm:ss"
 * @return
 */
