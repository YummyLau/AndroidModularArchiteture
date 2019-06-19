package com.effective.android.base.carsh

import android.content.Context

/**
 * crash相关管理
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2019/06/16.
 */
object CrashHelper {

    private var javaCrashHandler: JavaCrashHandler? = null
    private var nativeCrashHandler: NativeCrashHandler? = null

    fun setup(context: Context) {
        if (javaCrashHandler == null) {
            javaCrashHandler = JavaCrashHandler(context)
        }
        if (nativeCrashHandler == null) {
            nativeCrashHandler = NativeCrashHandler()
        }
    }
}