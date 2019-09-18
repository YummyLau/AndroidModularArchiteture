package com.effective.android.base.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by yummylau on 2018/3/9.
 */

object DisplayUtils {

    @JvmStatic
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}
