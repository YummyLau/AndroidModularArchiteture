package com.effective.android.component.weibo.util;

import com.effective.android.base.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {

    /**
     * weibo status's createtime should be transformed.
     *
     * @param createTime
     * @return
     */
    public static String transformTime(String createTime) {
        //Mon Dec 25 23:38:25 +0800 2017
        String datePart = "EEE MMM dd HH:mm:ss Z yyyy";
        String timeZonePart = "Z";
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePart, Locale.ENGLISH);
        SimpleDateFormat timeZoneFormat = new SimpleDateFormat(timeZonePart, Locale.ENGLISH);
        try {
            //微博创建时date
            Date date = dateFormat.parse(createTime);
            //取出创建时时区
            String dateTimeZone = timeZoneFormat.format(date);
            //换算当前时区与创建时区
            date = DateUtils.changeTimeZone(date, TimeZone.getTimeZone(dateTimeZone), TimeZone.getDefault());
            return DateUtils.getDiffTime(date.getTime());
        } catch (Exception e) {
            return createTime;
        }
    }
}
