package com.effective.android.video.core

import com.effective.android.video.bean.VideoCache

/**
 * 定义业务中播放器所需要的业务行为
 * created by yummylau 2019/04/20
 */
interface VideoPlayer {

    val volume : Float

    val duration : Long

    val currentPosition : Long

    val contentPosition : Long

    val isPlaying : Boolean

    val isPause : Boolean

    fun start(cache: VideoCache)

    fun play()

    fun pause()

    fun stop()

    fun seekTo(position: Long)

    fun release()
}
