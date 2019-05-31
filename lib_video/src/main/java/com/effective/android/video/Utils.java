package com.effective.android.video;

public class Utils {

    /**
     * mc时长转成 xx：xx：xx格式
     *
     * @param mc 毫秒单位
     * @return
     */
    public static String getVideoTimeStr(long mc) {
        if (mc < 0) {
            return "00:00";
        }
        int second = (int) mc / 1000;
        int hour = second / 3600;
        int min = (second % 3600) / 60;
        second = (second % 60);
        final String hourStr = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);
        final String minStr = min < 10 ? "0" + String.valueOf(min) : String.valueOf(min);
        final String secondStr = second < 10 ? "0" + String.valueOf(second) : String.valueOf(second);
        String result;
        if (hour != 0) {
            result = hourStr + ":" + minStr + ":" + secondStr;
        } else if (min != 0) {
            result = minStr + ":" + secondStr;
        } else {
            result = "00:" + secondStr;
        }
        return result;
    }

    /**
     * 显示剩余时长
     *
     * @param totalMc
     * @param positionMc
     * @return
     */
    public static String getRemainingVideoTimeStr(long totalMc, long positionMc) {
        return getVideoTimeStr(totalMc - positionMc);
    }
}
