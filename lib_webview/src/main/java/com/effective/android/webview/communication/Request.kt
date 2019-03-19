package com.effective.android.webview.communication

import com.google.gson.annotations.SerializedName

/**
 * 封装请求参数，区别ios和android平台
 * Created by yummylau on 2018/5/7.
 */
data class Request<T>(
        @SerializedName("iosVersion") var iosVersion: String,
        @SerializedName("androidVersion") var androidVersion: String,
        @SerializedName("params") var data: T)
