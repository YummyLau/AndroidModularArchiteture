package com.effective.android.video.ui.cover;

/**
 *  浮层ui层传递事件控制视频
 *  Created by g8931 on 2019/04/20.
 */
public interface ICoverControlLayer {

    void pause();

    void play();

    void seekTo(long position);

    void enterFullScreen();

    void exitFullScreen();

    boolean isFullScreen();
}
