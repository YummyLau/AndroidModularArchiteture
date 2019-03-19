package com.effective.android.base.util.view

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
        if (context == null)
            return null
        else if (context is Activity)
            return context
        else if (context is ContextWrapper)
            return scanForActivity(context.baseContext)

        return null
    }
}
