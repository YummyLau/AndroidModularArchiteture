package com.effective.android.video

import android.app.Application

class TestVideoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        VideoPlayerManager.init(this)
    }
}
