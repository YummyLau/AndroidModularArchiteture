package com.effective.android.base.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * activity工具类
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

object ActivityUtils {

    fun scanForActivity(context: Context?): Activity? {
        return when (context) {
            is Activity -> context
            is ContextWrapper -> scanForActivity(context)
            else -> null
        }
    }
}
