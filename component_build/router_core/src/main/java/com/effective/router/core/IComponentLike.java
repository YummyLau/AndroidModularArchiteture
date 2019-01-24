package com.effective.router.core;

/**
 * 组件声明周期
 * Created by yummyLau on 2018/8/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public interface IComponentLike {
    void onCreate();

    void onRelease();
}
