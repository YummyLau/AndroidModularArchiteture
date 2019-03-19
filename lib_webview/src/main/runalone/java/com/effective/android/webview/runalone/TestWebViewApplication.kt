package com.effective.android.webview.runalone

import android.support.multidex.MultiDexApplication
import com.effective.android.webview.X5WebViewManager

class TestWebViewApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        X5WebViewManager.initX5Webkit(this)
    }
}
