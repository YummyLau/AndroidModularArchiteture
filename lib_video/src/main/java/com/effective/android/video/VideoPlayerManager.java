package com.effective.android.video;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.effective.android.video.bean.VideoCache;
import com.effective.android.video.bean.VideoStatus;
import com.effective.android.video.core.DefaultControlLayer;
import com.effective.android.video.core.DefaultVideoPlayer;
import com.effective.android.video.core.IControlLayer;
import com.effective.android.video.core.IPlayer;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * 单例设计
 * created by yummylau 2019/04/20
 * 功能包含：
 * 1. 加载视频功能，支持多个场景下source缓存加载，保存视频播放进度
 * 2. 提供外层接收视频信息api
 * 2.1 网络事件切换见提供，包括 wifi/移动网络切换/网速均衡
 * 2.2 视频加载流程事件
 * 3. 提供控制视频信息api
 * <p>
 * 业务层适配：
 * 1. 根据接收适配信息api显示业务ui
 * 2. 根据业务ui的触发事件调用控制视频信息api
 * created by g8931 2019/05/20
 * <p>
 * 难点一：多视频缓存信息保存
 * 难点二：不同页面切换，如何做到暂停，播放衔接。
 */
public class VideoPlayerManager {

    private static VideoPlayerManager sInstance;
    private static boolean inited = false;
    private static Application context;

    private IPlayer player;
    private IControlLayer controlLayer;
    private Map<Long, VideoCache> videoCacheMap;

    private VideoCache lastCache;
    private VideoStatus lastStatus;
    private boolean pauseByUserAction;


    private VideoPlayerManager(Context context) {
        player = new DefaultVideoPlayer(context);
        controlLayer = new DefaultControlLayer(player);
        videoCacheMap = new HashMap<>();
    }

    public static void init(Application application) {
        if (inited) {
            return;
        }
        context = application;
    }


    public static VideoPlayerManager getInstance() {
        if (!inited) {
            throw new RuntimeException("you should invoke init() before invoking getInstance() ! ");
        }
        if (sInstance == null) {
            synchronized (VideoPlayerManager.class) {
                if (sInstance == null) {
                    sInstance = new VideoPlayerManager(context);
                }
            }
        }
        return sInstance;
    }

    private boolean isValidCache(VideoCache newCache) {
        return newCache != null && newCache.isValid();
    }

    private boolean isContinuePlayLastCache(VideoCache lastCache, VideoCache newCache) {
        return isValidCache(lastCache) && isValidCache(newCache)
                && newCache.isContinuePlaySame() && newCache.isSameContent(lastCache);
    }

    /**
     * 播放入口
     *
     * @param newCache
     */
    public void start(@NonNull VideoCache newCache) {
        if (!isValidCache(newCache)) {
            Log.d(Contants.TAG, "start a newCache that isn't valid!");
            return;
        }

        //重复启动，则需要读取缓存配置
        VideoCache cacheItem = videoCacheMap.get(newCache.getId());
        if (cacheItem != null) {
            newCache.lastStatus = cacheItem.lastStatus;
            newCache.lastPosition = cacheItem.lastPosition;
        }

        //如果支持续播上一个视频，则读取当前播放器的进度
        if (isContinuePlayLastCache(lastCache, newCache)) {
            newCache.lastPosition = player.getCurrentPosition();
        }

        //缓存当前正在处理的视频信息
        if (isValidCache(lastCache)) {
            lastCache.isMute = player.getVolume() == 0f;
            lastCache.lastPosition = player.getCurrentPosition();
            if (lastStatus == VideoStatus.PAUSE && !pauseByUserAction) {
                lastStatus = VideoStatus.PLAYING;
            }
            lastCache.lastStatus = lastStatus;
            player.stop();
        }

        //缓存并重制信息
        pauseByUserAction = false;
        lastCache = newCache;
        videoCacheMap.put(lastCache.getId(), lastCache);

        //启动视频
        player.start(lastCache);
    }
}
