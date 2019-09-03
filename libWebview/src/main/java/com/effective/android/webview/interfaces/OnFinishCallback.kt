package com.effective.android.webview.interfaces

interface OnFinishCallback {
    fun onSuccess(url: String)
    fun onFail(url: String)
}
