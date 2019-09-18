package com.effective.android.service.net

import com.google.gson.annotations.SerializedName

/**
 * postman抓取wanAndroid接口格式
 * 不同app的serviceNet设计不同，自行扩展
 * Created by yummyLau on 2018/7/30.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
open class BaseResult<T> {

    @SerializedName("errorCode")
    var errorCode: Int = 0

    @SerializedName("errorMsg")
    var errorMsg: String? = null

    @SerializedName("data")
    var data: T? = null

    val isSuccess: Boolean
        get() = errorCode == 0 && data != null
}
