package com.effective.android.net


import java.nio.charset.Charset

/**
 * Created by yummyLau on 2018/7/30.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */

object Constants {

    val UTF8 = Charset.forName("UTF-8")
    val MAX_AGE = 10
    val MAX_STALE = 60

    val READ_TIME_OUT = 30
    val WRITE_TIME_OUT = 30
    val CONNECT_TIME_OUT = 15

    val READ_TIME_OUT_FAST = 5
    val WRITE_TIME_OUT_FAST = 5
    val CONNECT_TIME_OUT_FAST = 5
}
