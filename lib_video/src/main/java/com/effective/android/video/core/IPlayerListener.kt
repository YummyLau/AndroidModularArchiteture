package com.effective.android.video.core

import com.effective.android.video.bean.VideoStatus

interface IPlayerListener {


    fun onLoadingChanged(isLoading: Boolean)

    fun onPlayerStateChanged(videoStatus: VideoStatus?)
}
