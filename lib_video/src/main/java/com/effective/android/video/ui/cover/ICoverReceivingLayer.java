package com.effective.android.video.ui.cover;

/**
 *  浮层ui层变化
 *  Created by g8931 on 2019/04/20.
 */
public interface ICoverReceivingLayer {

    void onCoverClick();

    void onControlVisible(boolean visible);

    boolean onBack();

    boolean onFullScreen(boolean unfold);
}
