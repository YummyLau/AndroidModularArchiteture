package com.effective.android.net

import okhttp3.MediaType

/**
 * Created by yummyLau on 2018/5/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
object MediaTypes {

    val JSON = MediaType.parse("application/json; charset=utf-8")

    val FILE = MediaType.parse("application/octet-stream")

    val STREAM = MediaType.parse("application/octet-stream")

    val TEXT = MediaType.parse("text/plain")
}
