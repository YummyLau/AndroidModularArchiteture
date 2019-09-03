package com.effective.android.webview.jsbridge

import com.effective.android.webview.interfaces.BridgeHandler
import com.effective.android.webview.interfaces.CallBackFunction

class DefaultHandler : BridgeHandler {

    internal var TAG = "DefaultHandler"

    override fun handler(data: String?, function: CallBackFunction) {
        function?.onCallBack("DefaultHandler response data")
    }
}
