package com.effective.android.video.interfaces;

import com.effective.android.video.bean.VideoCache;

/**
 * 视图层通过该接口控制播放器行为
 * created by yummylau 2019/04/20
 */
public interface IControlLayer {

    void restore(VideoCache videoCache);

    void replay(VideoCache videoCache);

    void play(VideoCache videoCache);

    void pause(VideoCache videoCache,boolean userAction);

    void stop(VideoCache videoCache);

    void release(VideoCache videoCache);

    void seekTo(VideoCache videoCache, long position);
}
