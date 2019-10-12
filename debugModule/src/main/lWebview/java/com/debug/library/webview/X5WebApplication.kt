package com.debug.library.webview

import com.debug.DebugApplication
import com.effective.android.webview.Utils

class X5WebApplication :DebugApplication(){

    override fun onCreate() {
        super.onCreate()
        Utils.initX5Webkit(this)
    }
}