package com.effective.android.video.core

import com.effective.android.video.bean.VideoCache

/**
 * 视图层通过该接口控制播放器行为
 * created by yummylau 2019/04/20
 */
interface VideoControlLayer {

    fun restore(videoCache: VideoCache)

    fun replay(videoCache: VideoCache)

    fun play(videoCache: VideoCache)

    fun pause(videoCache: VideoCache, userAction: Boolean)

    fun stop(videoCache: VideoCache)

    fun release(videoCache: VideoCache)

    fun seekTo(videoCache: VideoCache, position: Long)

    fun isVideoLoaded(): Boolean             //播放器是否加载了视频

    fun isPlaying(): Boolean

    fun isPause(): Boolean

    fun getDuration(): Long

    fun getContentPosition(): Long           //含广告

    fun getCurrentPosition(): Long      //当前进度
}
