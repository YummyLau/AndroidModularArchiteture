package com.effective.android.base.rxjava

import android.util.Log

import io.reactivex.plugins.RxJavaPlugins

/**
 * 在app启动的时候默认处理
 * Created by yummyLau on 2019/6/20.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
object AppRxPlugins {

    private val TAG = AppRxPlugins::class.java.simpleName

    @JvmStatic
    fun init() {
        RxJavaPlugins.setErrorHandler {
            Log.e(TAG, "error :" + it.message)
        }
    }
}