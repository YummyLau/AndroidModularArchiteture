package com.effective.android.video.core


import android.content.Context
import android.net.Uri
import android.os.Environment

import com.effective.android.video.bean.VideoCache
import com.effective.android.video.bean.VideoStatus
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util

import java.io.File

/**
 * 默认实现的播放器
 * 可自主实现ViewoPlayer接口完成播放器行为的实现，这里使用ExoPlayer进行实现
 * created by yummylau 2019/06/03
 */
class DefaultVideoPlayer(private val context: Context) : VideoPlayer {

    val player: SimpleExoPlayer
    private val eventListener = InnerListener()
    private val listeners: MutableList<VideoPlayerListener>

    init {
        this.player = SimpleExoPlayerFixMute.make(context, SimpleExoLoadControl())
        this.player.addListener(eventListener)
        this.listeners = arrayListOf()
    }

    /**
     * 添加监听播放器行为
     */
    override fun addPlayerListener(listener: VideoPlayerListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    /**
     * 移除播放器行为监听
     */
    override fun remotePlayerListener(listener: VideoPlayerListener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener)
        }
    }

    override fun start(cache: VideoCache) {
        val mediaSource = MediaSourceFactory.create(context, cache.videoInfo.url, cache.isCache, cache.isLoop)
        player.setVolume(if (cache.isMute) 0f else 0.5f)
        eventListener.hasInit = false
        player.prepare(mediaSource)
        player.seekTo(cache.lastPosition)
        player.playWhenReady = if (cache.lastStatus !== VideoStatus.PAUSE) true else false
    }

    override fun play() {
        player.playWhenReady = true
    }

    override fun pause() {
        player.playWhenReady = false
    }

    override fun stop() {
        player.stop()
    }

    override fun seekTo(position: Long) {
        player.seekTo(position)
    }

    override fun release() {
        player.removeListener(eventListener)
        player.release()
        listeners.clear()
    }

    override val volume: Float
        get() = player.volume

    override val duration: Long
        get() = player.duration

    override val currentPosition: Long
        get() = player.currentPosition

    override val contentPosition: Long
        get() = player.contentPosition

    override val isPlaying: Boolean
        get() = player.playWhenReady

    override val isPause: Boolean
        get() = player.playWhenReady

    override val bufferedPostion: Long
        get() = player.bufferedPosition
    /**
     * ExoPlayer播放器监听播放事件
     */
    private inner class InnerListener : Player.EventListener {

        internal var hasInit = false

        override fun onLoadingChanged(isLoading: Boolean) {
            for (playerListener in listeners) {
                playerListener.onLoadingChanged(isLoading)
            }
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            var videoStatus: VideoStatus? = null
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    videoStatus = VideoStatus.BUFFERING
                }
                Player.STATE_READY -> {
                    if (!hasInit && playWhenReady) {
                        hasInit = true
                        for (playerListener in listeners) {
                            playerListener.onPlayerStateChanged(VideoStatus.FIRST_FRAME)
                        }
                    }
                    if (playWhenReady) {
                        videoStatus = VideoStatus.PLAYING
                    } else {
                        videoStatus = VideoStatus.PAUSE
                    }
                }
                Player.STATE_ENDED -> {
                    videoStatus = VideoStatus.FINISH
                }
            }
            for (playerListener in listeners) {
                playerListener.onPlayerStateChanged(videoStatus)
            }
        }

        override fun onPlayerError(error: ExoPlaybackException?) {
            for (playerListener in listeners) {
                playerListener.onPlayerStateChanged(VideoStatus.ERROR)
            }
        }
    }

    /**
     * 构建 mediasource 对象入口
     */
    private object MediaSourceFactory {

        private val CACHE_MAX_SIZE = 1024 * 1024 * 1024        // 1GB缓存
        private var cache: Cache? = null

        @JvmOverloads
        fun create(context: Context, url: String?, fileCache: Boolean = true, loop: Boolean = false): MediaSource {
            var url = url
            if (url == null) {
                url = ""
            }
            var dataSourceFactory: DataSource.Factory = DefaultHttpDataSourceFactory(
                    Util.getUserAgent(context, "lib_video"), null,
                    DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                    DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                    true)
            dataSourceFactory = DefaultDataSourceFactory(context, null, dataSourceFactory)
            if (fileCache) {
                val cache = getCache(context)
                if (cache != null) {
                    dataSourceFactory = CacheDataSourceFactory(cache, dataSourceFactory)
                }
            }
            val factory = ExtractorMediaSource.Factory(dataSourceFactory)
            var mediaSource: MediaSource = factory.createMediaSource(Uri.parse(url))
            if (loop) {
                mediaSource = LoopingMediaSource(mediaSource)
            }
            return mediaSource
        }

        fun getCache(context: Context): Cache? {
            if (cache == null) {
                try {
                    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                        val cacheRoot = context.externalCacheDir
                        if (cacheRoot != null) {
                            val exoCache = File(cacheRoot, "exo_cache")
                            if (!exoCache.exists()) {
                                exoCache.mkdirs()
                            }
                            val cacheEvictor = LeastRecentlyUsedCacheEvictor(CACHE_MAX_SIZE.toLong())
                            cache = SimpleCache(exoCache, cacheEvictor)
                        }
                    } else {
                        return null
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            return cache
        }
    }
}
