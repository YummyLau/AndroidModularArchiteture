package com.effective.android.video

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.annotation.NonNull

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


    fun getWindowWidth(context: Context): Int {
        val manager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    fun getWindowHeight(context: Context): Int {
        val manager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    fun scanForActivity(@NonNull context: Context?): Activity? {
        if (context == null) {
            return null
        } else if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return scanForActivity(context.baseContext)
        }
        return null
    }


    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}
