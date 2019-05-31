package com.effective.android.video.core

import com.effective.android.video.bean.VideoCache

/**
 * 定义业务中播放器所需要的业务行为
 * created by yummylau 2019/04/20
 */
interface IPlayer {

    val currentPosition: Long

    val volume: Float

    fun start(cache: VideoCache)

    fun play()

    fun pause()

    fun stop()

    fun seekTo(position: Long)

    fun release()
}
