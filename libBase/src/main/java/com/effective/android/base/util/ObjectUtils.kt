package com.effective.android.base.util

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
object ObjectUtils {

    @JvmStatic
    fun equals(o1: Any?, o2: Any?): Boolean {
        if (o1 == null) {
            return o2 == null
        }
        return if (o2 == null) {
            false
        } else o1 == o2
    }
}
