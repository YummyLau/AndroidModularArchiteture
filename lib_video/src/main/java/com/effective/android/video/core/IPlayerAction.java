package com.effective.android.video.core;

/**
 * 定义业务中播放器所需要的业务行为
 * created by yummylau 2019/04/20
 */
public interface IPlayerAction {

    void play();

    void pause();

    void stop();

    void seekTo(long position);

    void release();
}
