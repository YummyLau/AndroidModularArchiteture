package com.effective.android.video.ui.cover

import com.effective.android.video.bean.VideoStatus

interface VideoStatusListener {
    fun onStatus(videoStatus: VideoStatus?, msg: String)
}
