package com.effective.android.base.util

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.text.TextUtils

/**
 * activity工具类
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

object ActivityUtils {

    @JvmStatic
    fun scanForActivity(context: Context?): Activity? {
        return when (context) {
            is Activity -> context
            is ContextWrapper -> scanForActivity(context)
            else -> null
        }
    }

    @JvmStatic
    fun isForeground(context: Context?, className: String): Boolean {
        if (context == null || TextUtils.isEmpty(className)) {
            return false
        }
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = am.getRunningTasks(1)
        if (list != null && list.size > 0) {
            val cpn = list[0].topActivity
            if (className == cpn.className) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun isPortrait(context: Context?): Boolean {
        return if (context == null) false else context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }
}
