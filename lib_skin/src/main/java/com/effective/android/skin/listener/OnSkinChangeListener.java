package com.effective.android.skin.listener;

import android.support.annotation.Nullable;

import com.effective.android.skin.bean.SkinItem;

/**
 * 回调皮肤是否切换成功
 * Created by yummyLau on 2018/9/6.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public interface OnSkinChangeListener {

    void onChange(boolean success, @Nullable SkinItem skin);
}
