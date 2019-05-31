package com.effective.android.video

object Utils {

    /**
     * mc时长转成 xx：xx：xx格式
     *
     * @param mc 毫秒单位
     * @return
     */
    fun getVideoTimeStr(mc: Long): String {
        if (mc < 0) {
            return "00:00"
        }
        var second = mc.toInt() / 1000
        val hour = second / 3600
        val min = second % 3600 / 60
        second = second % 60
        val hourStr = if (hour < 10) "0$hour" else hour.toString()
        val minStr = if (min < 10) "0$min" else min.toString()
        val secondStr = if (second < 10) "0$second" else second.toString()
        val result: String
        if (hour != 0) {
            result = "$hourStr:$minStr:$secondStr"
        } else if (min != 0) {
            result = "$minStr:$secondStr"
        } else {
            result = "00:$secondStr"
        }
        return result
    }

    /**
     * 显示剩余时长
     *
     * @param totalMc
     * @param positionMc
     * @return
     */
    fun getRemainingVideoTimeStr(totalMc: Long, positionMc: Long): String {
        return getVideoTimeStr(totalMc - positionMc)
    }
}
