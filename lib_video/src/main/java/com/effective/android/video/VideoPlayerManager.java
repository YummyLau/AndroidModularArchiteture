package com.effective.android.video;


import android.content.Context;

import com.effective.android.video.core.DefaultCorePlayer;
import com.effective.android.video.core.IPlayerAction;

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

    private static String TAG = VideoPlayerManager.class.getSimpleName();

    private static VideoPlayerManager sInstance;
    private IPlayerAction player;

    public VideoPlayerManager(Context context) {
        player = new DefaultCorePlayer(context);
    }

    public static VideoPlayerManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (VideoPlayerManager.class) {
                if (sInstance == null) {
                    sInstance = new VideoPlayerManager(context);
                }
            }
        }
        return sInstance;
    }
}
