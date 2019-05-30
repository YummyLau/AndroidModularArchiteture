package com.effective.android.video.core;

import com.effective.android.video.bean.VideoCache;

public class DefaultControlLayer implements IControlLayer {

    private IPlayer player;

    public DefaultControlLayer(IPlayer player) {
        this.player = player;
    }

    @Override
    public void restore(VideoCache videoCache) {

    }

    @Override
    public void replay(VideoCache videoCache) {

    }

    @Override
    public void play(VideoCache videoCache) {

    }

    @Override
    public void pause(VideoCache videoCache, boolean userAction) {

    }

    @Override
    public void stop(VideoCache videoCache) {

    }

    @Override
    public void release(VideoCache videoCache) {

    }

    @Override
    public void seekTo(VideoCache videoCache, long position) {

    }
}
