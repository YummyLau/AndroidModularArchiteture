package com.effective.android.video.core

import com.effective.android.video.bean.VideoStatus

/**
 * 业务外部可监听播放器事件
 */
interface VideoPlayerListener {

    fun onLoadingChanged(isLoading: Boolean)

    fun onPlayerStateChanged(videoStatus: VideoStatus?)
}
