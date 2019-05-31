package com.effective.android.video.ui.cover;

import com.effective.android.video.bean.VideoStatus;

import androidx.annotation.Nullable;

public interface VideoStatusListener {
    void onStatus(@Nullable VideoStatus videoStatus, String msg);
}
