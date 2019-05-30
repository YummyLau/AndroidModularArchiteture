package com.effective.android.video.core;

import com.effective.android.video.bean.VideoStatus;
import com.google.android.exoplayer2.ExoPlayer;

import androidx.annotation.Nullable;

/**
 * 视图层通过该接口接受播放器信息
 * created by yummylau 2019/04/20
 */
public interface IReceivingLayer {

    void attachedControlLayer(IControlLayer controlLayer);

    void attachPlayer(ExoPlayer player);

    void detachPlayer(ExoPlayer player);

    void releaseControlLayer();

    void onVideoStatus(VideoStatus status, @Nullable String message);

    void onBufferPosition(long bufferedPosition);

    void onFirstInit();

    void clickToPlay();

    void clickToPause();
}
