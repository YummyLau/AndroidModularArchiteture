package com.effective.android.video.core;

import com.effective.android.video.bean.VideoCache;

/**
 * 定义业务中播放器所需要的业务行为
 * created by yummylau 2019/04/20
 */
public interface IPlayer {

    void start(VideoCache cache);

    void play();

    void pause();

    void stop();

    void seekTo(long position);

    void release();

    long getCurrentPosition();

    float getVolume();
}
