package com.effective.android.video.bean;

import androidx.annotation.NonNull;

/**
 * 定义单例加载的视频信息及缓存信息
 * created by yummylau 2019/04/20
 */
public class VideoCache {

    public long id;
    public boolean isCache;
    public boolean isLoop;
    public boolean isMute;
    public boolean isContinuePlaySame;

    public long lastPosition;
    public VideoStatus lastStatus;

    public VideoInfo videoInfo;

    public VideoCache(Builder builder) {
        this.isCache = builder.isCache;
        this.isLoop = builder.isLoop;
        this.isMute = builder.isMute;
        this.isContinuePlaySame = builder.isContinuePlaySame;
        this.videoInfo = builder.videoInfo;
        this.id = System.currentTimeMillis();
    }


    public static class Builder {
        private boolean isCache = true;               //是否缓存
        private boolean isMute = true;                 //是否静音
        private boolean isLoop = true;                 //是否循环播放
        private boolean isContinuePlaySame = true;     //是否继续播放，两个页面相同视频
        private VideoInfo videoInfo;

        public Builder cache(boolean cache) {
            this.isCache = cache;
            return this;
        }

        public Builder loop(boolean loop) {
            this.isLoop = loop;
            return this;
        }

        public Builder mute(boolean mute) {
            this.isMute = mute;
            return this;
        }

        public Builder continuePlaySame(boolean continuePlaySame) {
            this.isContinuePlaySame = continuePlaySame;
            return this;
        }

        public VideoCache build(@NonNull VideoInfo videoInfo) {
            this.videoInfo = videoInfo;
            return new VideoCache(this);
        }
    }

}
