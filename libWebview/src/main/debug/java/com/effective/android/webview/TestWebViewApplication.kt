package com.effective.android.webview

import android.app.Application

class TestWebViewApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        X5WebViewManager.initX5Webkit(this)
    }
}
