package com.effective.android.video.core;


import android.content.Context;

import com.effective.android.video.bean.VideoStatus;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class DefaultCorePlayer implements IPlayerAction {

    private SimpleExoPlayer player;
    private Player.EventListener eventListener = new Player.EventListener() {

        boolean hasInit = false;

        @Override
        public void onLoadingChanged(boolean isLoading) {
            for (IPlayerListener playerListener : listeners) {
                playerListener.onLoadingChanged(isLoading);
            }
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            VideoStatus videoStatus = null;
            switch (playbackState) {
                case Player.STATE_BUFFERING: {
                    videoStatus = VideoStatus.BUFFERING;
                    break;
                }
                case Player.STATE_READY: {
                    if (!hasInit && playWhenReady) {
                        hasInit = true;
                        videoStatus = VideoStatus.FIRST_FRAME;
                    }
                    if (playWhenReady) {
                        videoStatus = VideoStatus.PLAYING;
                    } else {
                        videoStatus = VideoStatus.PAUSE;
                    }
                    break;
                }
                case Player.STATE_ENDED: {
                    videoStatus = VideoStatus.FINISH;
                    break;
                }
            }
            for (IPlayerListener playerListener : listeners) {
                playerListener.onPlayerStateChanged(videoStatus);
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            for (IPlayerListener playerListener : listeners) {
                playerListener.onPlayerStateChanged(VideoStatus.ERROR);
            }
        }
    };
    private List<IPlayerListener> listeners;

    public DefaultCorePlayer(Context context) {
        this.player = SimpleExoPlayerFixMute.make(context, new SimpleExoLoadControl());
        this.player.addListener(eventListener);
        this.listeners = new ArrayList<>();
    }

    public void addPlayerListener(@NonNull IPlayerListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void remotePlayerListener(@NonNull IPlayerListener listener) {
        if (listener != null && listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }


    @Override
    public void play() {
        player.setPlayWhenReady(true);
    }

    @Override
    public void pause() {
        player.setPlayWhenReady(false);
    }

    @Override
    public void stop() {
        player.stop();
    }

    @Override
    public void seekTo(long position) {

    }

    @Override
    public void release() {
        player.removeListener(eventListener);
        player.release();
        listeners.clear();
    }
}
