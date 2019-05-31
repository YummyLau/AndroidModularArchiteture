package com.effective.android.video.core

import com.effective.android.video.bean.VideoStatus
import com.google.android.exoplayer2.ExoPlayer

/**
 * 视图层通过该接口接受播放器信息
 * created by yummylau 2019/04/20
 */
interface IReceivingLayer {

    fun attachedControlLayer(controlLayer: IControlLayer)

    fun attachPlayer(player: ExoPlayer)

    fun detachPlayer(player: ExoPlayer)

    fun releaseControlLayer()

    fun onVideoStatus(status: VideoStatus, message: String?)

    fun onBufferPosition(bufferedPosition: Long)

    fun onFirstInit()

    fun clickToPlay()

    fun clickToPause()
}
