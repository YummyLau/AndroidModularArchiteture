package com.effective.android.video.core;

import com.effective.android.video.bean.VideoStatus;

import androidx.annotation.Nullable;

public interface IPlayerListener {


    void onLoadingChanged(boolean isLoading);

    void onPlayerStateChanged(@Nullable VideoStatus videoStatus);
}
