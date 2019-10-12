package com.debug.library.webview

import android.os.Bundle
import com.debug.R
import com.effective.android.base.activity.BaseActivity
import com.effective.android.webview.Utils
import com.effective.android.webview.X5WebChromeClient
import kotlinx.android.synthetic.main.web_main_layout.*

class X5WebActivity : BaseActivity(){

    override fun getLayoutRes(): Int = R.layout.web_main_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setDefaultWebViewSetting(webview)
        webview.webChromeClient = X5WebChromeClient()
        webview.loadUrl("https://www.baidu.com")
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils.destroyWebView(webview)
    }
}