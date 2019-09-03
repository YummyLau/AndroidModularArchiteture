package com.effective.android.video.ui.cover

import com.effective.android.video.bean.VideoInfo

/**
 * 浮层ui层传递事件控制视频
 * Created by yummylau on 2019/04/20.
 */
interface CoverControlLayer {

    val isVideoLoaded: Boolean            //播放器是否加载了视频

    val isMediaInfoLoaded: Boolean        //是否设置了info

    val isPlaying: Boolean

    val isPause: Boolean

    val videoDuration: Long            //播放器加载之后返回的时长

    val mediaDuration: Long            //info中的长度

    val duration: Long

    val contentPosition: Long          //含广告

    val videoCurrentPosition: Long     //当前进度

    val videoInfo: VideoInfo?

    fun isFullScreen(): Boolean

    fun pause()

    fun play()

    fun seekTo(position: Long)

    fun enterFullScreen()

    fun exitFullScreen()

    fun makeSurePlay()
}
