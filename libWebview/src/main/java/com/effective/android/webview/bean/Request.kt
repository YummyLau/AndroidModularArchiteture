package com.effective.android.webview.bean

import com.google.gson.annotations.SerializedName

/**
 * 封装请求参数，区别ios和android平台
 * Created by yummylau on 2018/5/7.
 */
data class Request<T>(
        @SerializedName("platform") var platform: String,
        @SerializedName("version") var version: String,
        @SerializedName("params") var data: T)
