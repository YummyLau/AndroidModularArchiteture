package example.basiclib.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.Nullable;

/**
 * data utils
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class DateUtils {

    private static final String TAG = DateUtils.class.getSimpleName();

    private final static int TIME_MILLISECONDS = 1000;
    private final static int TIME_NUMBERS = 60;
    private final static int TIME_HOURSES = 24;
    private final static String FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    /**
     * @param date 日期字符串，必须为"yyyy-MM-dd HH:mm:ss"
     * @param date
     * @return
     */
    public static Date parseDate(String date) {
        return parseDate(date, FORMAT);
    }

    @Nullable
    public static Date parseDate(String date, String format) {
        Date dt = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            dt = dateFormat.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return dt;
    }

    /**
     * @param date 日期字符串，必须为"yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String formatDate(Date date) {
        return formatDate(date, FORMAT);
    }


    public static String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }


    public static String formatUnixTime(long unixTime) {
        return formatUnixTime(unixTime, FORMAT);
    }

    public static String formatUnixTime(long unixTime, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(unixTime);
    }

    public static String formatGMTUnixTime(long gmtUnixTime) {
        return formatGMTUnixTime(gmtUnixTime, FORMAT);
    }

    public static String formatGMTUnixTime(long gmtUnixTime, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(gmtUnixTime + TimeZone.getDefault().getRawOffset());
    }

    public static Date getDate(long unixTime) {
        return new Date(unixTime);
    }

    public static Date getGMTDate(long gmtUnixTime) {
        return new Date(gmtUnixTime + TimeZone.getDefault().getRawOffset());
    }

    public static long getGMTUnixTime(long unixTime) {
        return unixTime - TimeZone.getDefault().getRawOffset();
    }

    /**
     * 将GMT Unix时间戳转换为系统默认时区的Unix时间戳
     *
     * @param gmtUnixTime
     * @return
     */
    public static long getCurrentTimeZoneUnixTime(long gmtUnixTime) {
        return gmtUnixTime + TimeZone.getDefault().getRawOffset();
    }

    public static long getGMTUnixTimeByCalendar() {
        Calendar calendar = Calendar.getInstance();
        long unixTime = calendar.getTimeInMillis();
        long unixTimeGMT = unixTime - TimeZone.getDefault().getRawOffset();
        return unixTimeGMT;
    }

    public static long getUnixTimeByCalendar() {
        Calendar calendar = Calendar.getInstance();
        long unixTime = calendar.getTimeInMillis();
        return unixTime;
    }

    public static Date changeTimeZone(Date date, TimeZone oldZone, TimeZone newZone) {
        Date dateTmp = null;
        if (date != null) {
            int timeOffset = oldZone.getRawOffset() - newZone.getRawOffset();
            dateTmp = new Date(date.getTime() - timeOffset);
        }
        return dateTmp;
    }

    public static String formatTime(long seconds) {
        long hh = seconds / TIME_NUMBERS / TIME_NUMBERS;
        long mm = (seconds - hh * TIME_NUMBERS * TIME_NUMBERS) > 0 ? (seconds - hh * TIME_NUMBERS * TIME_NUMBERS) / TIME_NUMBERS : 0;
        long ss = seconds < TIME_NUMBERS ? seconds : seconds % TIME_NUMBERS;
        return (hh == 0 ? "" : (hh < 10 ? "0" + hh : hh) + "小时")
                + (mm == 0 ? "" : (mm < 10 ? "0" + mm : mm) + "分")
                + (ss == 0 ? "" : (ss < 10 ? "0" + ss : ss) + "秒");
    }

    public static String getDiffTime(long date) {
        String strTime = "很久很久以前";
        long time = Math.abs(new Date().getTime() - date);
        // 一分钟以内
        if (time < TIME_NUMBERS * TIME_MILLISECONDS) {
            strTime = "刚刚";
        } else {
            int min = (int) (time / TIME_MILLISECONDS / TIME_NUMBERS);
            if (min < TIME_NUMBERS) {
                if (min < 15) {
                    strTime = "一刻钟前";
                } else if (min < 30) {
                    strTime = "半小时前";
                } else {
                    strTime = "1小时前";
                }
            } else {
                int hh = min / TIME_NUMBERS;
                if (hh < TIME_HOURSES) {
                    strTime = hh + "小时前";
                } else {
                    int days = hh / TIME_HOURSES;
                    if (days <= 6) {
                        strTime = days + "天前";
                    } else {
                        int weeks = days / 7;
                        if (weeks < 3) {
                            strTime = weeks + "周前";
                        }
                    }
                }
            }
        }
        return strTime;
    }

}
