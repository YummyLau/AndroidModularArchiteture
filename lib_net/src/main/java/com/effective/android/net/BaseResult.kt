package com.effective.android.net

import com.google.gson.annotations.SerializedName

/**
 * 自主使用 postman 自定义 mockserver 测试
 * 统一管理返回值
 * {
 * "code" : 200,
 * "message" : "ok",
 * "data" : {
 * "a" : "a",
 * "b" : "b",
 * "c" : 1
 * }
 * }
 * Created by yummyLau on 2018/7/30.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class BaseResult<T> {

    @SerializedName("code")
    private val code: Int = 0

    @SerializedName("message")
    private val message: String? = null

    @SerializedName("data")
    private val data: T? = null

    val isSuccess: Boolean
        get() = code == 200
}
