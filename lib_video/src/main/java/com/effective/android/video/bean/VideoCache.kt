package com.effective.android.video.bean

import android.text.TextUtils

/**
 * 定义单例加载的视频信息及缓存信息
 * created by yummylau 2019/04/20
 */
class VideoCache(builder: Builder) {

    var id: Long = 0
    var isCache: Boolean = false
    var isLoop: Boolean = false
    var isMute: Boolean = false
    var isContinuePlaySame: Boolean = false

    var lastPosition: Long = 0
    var lastStatus: VideoStatus? = null

    var videoInfo: VideoInfo? = null

    val isValid: Boolean
        get() = videoInfo != null

    init {
        this.isCache = builder.isCache
        this.isLoop = builder.isLoop
        this.isMute = builder.isMute
        this.isContinuePlaySame = builder.isContinuePlaySame
        this.videoInfo = builder.videoInfo
        this.id = System.currentTimeMillis()
    }

    fun isSameContent(videoCache: VideoCache?): Boolean {
        return isValid && videoCache != null && videoCache.isValid && TextUtils.equals(videoCache.videoInfo!!.url, videoInfo!!.url)
    }

    class Builder {
        var isCache = true               //是否缓存
        var isMute = true                 //是否静音
        var isLoop = true                 //是否循环播放
        var isContinuePlaySame = true     //是否继续播放，两个页面相同视频
        var videoInfo: VideoInfo? = null

        fun cache(cache: Boolean): Builder {
            this.isCache = cache
            return this
        }

        fun loop(loop: Boolean): Builder {
            this.isLoop = loop
            return this
        }

        fun mute(mute: Boolean): Builder {
            this.isMute = mute
            return this
        }

        fun continuePlaySame(continuePlaySame: Boolean): Builder {
            this.isContinuePlaySame = continuePlaySame
            return this
        }

        fun build(videoInfo: VideoInfo): VideoCache {
            this.videoInfo = videoInfo
            return VideoCache(this)
        }
    }

}
