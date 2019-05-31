package com.effective.android.video.core

import com.effective.android.video.bean.VideoStatus

interface VideoPlayerListener {

    fun onLoadingChanged(isLoading: Boolean)

    fun onPlayerStateChanged(videoStatus: VideoStatus?)
}
