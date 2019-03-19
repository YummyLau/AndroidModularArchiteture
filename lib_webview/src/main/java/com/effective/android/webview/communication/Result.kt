package com.effective.android.webview.communication

import com.google.gson.annotations.SerializedName

/**
 * 无论是 js调native 还是native 调js，都统一 Result格式返回
 * Created by yummylau on 2018/5/7.
 */
data class Result<T>(
        @SerializedName("code") val code: Int,
        @SerializedName("msg") val msg: String,
        @SerializedName("msg") val data: T)
