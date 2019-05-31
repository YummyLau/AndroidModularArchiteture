package com.effective.android.video.ui.cover;

import com.effective.android.video.bean.VideoInfo;

import androidx.annotation.Nullable;

/**
 *  浮层ui层传递事件控制视频
 *  Created by yummylau on 2019/04/20.
 */
public interface ICoverControlLayer {

    void pause();

    void play();

    void seekTo(long position);

    void enterFullScreen();

    void exitFullScreen();

    boolean isFullScreen();

    boolean isVideoLoaded();            //播放器是否加载了视频

    boolean isMediaInfoLoaded();        //是否设置了info

    boolean isPlaying();

    boolean isPause();

    boolean isPrepare();

    long getVideoDuration();            //播放器加载之后返回的时长

    long getMediaDuration();            //info中的长度

    long getDuration();

    long getContentPosition();          //含广告

    long getVideoCurrentPosition();     //当前进度

    void makeSurePlay();

    @Nullable
    VideoInfo getVideoInfo();
}
