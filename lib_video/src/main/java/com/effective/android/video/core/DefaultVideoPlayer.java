package com.effective.android.video.core;


import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.effective.android.video.bean.VideoCache;
import com.effective.android.video.bean.VideoStatus;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheEvictor;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DefaultVideoPlayer implements IPlayer {

    private SimpleExoPlayer player;
    private InnerListener eventListener = new InnerListener();
    private List<IPlayerListener> listeners;
    private Context context;

    public DefaultVideoPlayer(Context context) {
        this.context = context;
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
    public void start(VideoCache cache) {
        if (cache != null && cache.isValid()) {
            MediaSource mediaSource = MediaSourceFactory.create(context, cache.getVideoInfo().url, cache.isCache, cache.isLoop);
            player.setVolume(cache.isMute ? 0 : 0.5f);
            eventListener.hasInit = false;
            player.prepare(mediaSource);
            player.seekTo(cache.lastPosition);
            player.setPlayWhenReady(cache.lastStatus != VideoStatus.PAUSE ? true : false);
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

    @Override
    public long getCurrentPosition() {
        return 0;
    }

    @Override
    public float getVolume() {
        return 0;
    }


    private class InnerListener implements Player.EventListener {

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
                        for (IPlayerListener playerListener : listeners) {
                            playerListener.onPlayerStateChanged(VideoStatus.FIRST_FRAME);
                        }
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
    }

    /**
     * 构建mediasource的工厂
     */
    private static class MediaSourceFactory {

        private static final int CACHE_MAX_SIZE = 1024 * 1024 * 1024;        // 1GB缓存
        private static Cache cache;

        public static MediaSource create(Context context, String url) {
            return create(context, url, true, false);
        }

        public static MediaSource create(Context context, String url, boolean fileCache) {
            return create(context, url, fileCache, false);
        }

        public static MediaSource create(Context context, String url, boolean fileCache, boolean loop) {
            if (url == null) {
                url = "";
            }
            DataSource.Factory dataSourceFactory =
                    new DefaultHttpDataSourceFactory(
                            Util.getUserAgent(context, "lib_video"),
                            null,
                            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                            true);
            dataSourceFactory = new DefaultDataSourceFactory(context, null, dataSourceFactory);
            if (fileCache) {
                Cache cache = getCache(context);
                if (cache != null) {
                    dataSourceFactory = new CacheDataSourceFactory(cache, dataSourceFactory);
                }
            }
            ExtractorMediaSource.Factory factory = new ExtractorMediaSource.Factory(dataSourceFactory);
            MediaSource mediaSource = factory.createMediaSource(Uri.parse(url));
            if (loop) {
                mediaSource = new LoopingMediaSource(mediaSource);
            }
            return mediaSource;
        }

        public static Cache getCache(Context context) {
            if (cache == null) {
                try {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File cacheRoot = context.getExternalCacheDir();
                        if (cacheRoot != null) {
                            File exoCache = new File(cacheRoot, "exo_cache");
                            if (!exoCache.exists()) {
                                exoCache.mkdirs();
                            }
                            CacheEvictor cacheEvictor = new LeastRecentlyUsedCacheEvictor(CACHE_MAX_SIZE);
                            cache = new SimpleCache(exoCache, cacheEvictor);
                        }
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return cache;
        }
    }
}
