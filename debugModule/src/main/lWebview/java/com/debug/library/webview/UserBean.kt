package com.debug.library.webview

import com.google.gson.annotations.SerializedName

data class UserBean(
        @SerializedName("userName") var userName: String,
        @SerializedName("password") var password: String,
        @SerializedName("caller") var caller: String)