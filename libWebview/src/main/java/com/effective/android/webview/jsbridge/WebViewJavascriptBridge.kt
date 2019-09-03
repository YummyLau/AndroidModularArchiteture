package com.effective.android.webview.jsbridge

import com.effective.android.webview.interfaces.CallBackFunction


interface WebViewJavascriptBridge {
    fun send(data: String?)
    fun send(data: String?, responseCallback: CallBackFunction?)
}
