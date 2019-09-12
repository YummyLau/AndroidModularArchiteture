package com.effective.android.net.okhttp.api


/**
 * 封装常规请求
 * Created by yummyLau on 2018/5/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
interface HttpApi {

    fun get(url: String): String

    fun post(url: String, json: String): String

    fun post(url: String, mapParams: Map<String, String>): String

    fun uploadFile(url: String, pathName: String, fileName: String): String
}
