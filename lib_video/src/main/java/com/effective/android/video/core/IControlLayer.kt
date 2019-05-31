package com.effective.android.video.core

import com.effective.android.video.bean.VideoCache

/**
 * 视图层通过该接口控制播放器行为
 * created by yummylau 2019/04/20
 */
interface IControlLayer {

    fun restore(videoCache: VideoCache)

    fun replay(videoCache: VideoCache)

    fun play(videoCache: VideoCache)

    fun pause(videoCache: VideoCache, userAction: Boolean)

    fun stop(videoCache: VideoCache)

    fun release(videoCache: VideoCache)

    fun seekTo(videoCache: VideoCache, position: Long)
}
