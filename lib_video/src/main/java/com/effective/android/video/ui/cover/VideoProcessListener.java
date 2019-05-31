package com.effective.android.video.ui.cover;

public interface VideoProcessListener {

    void onDuration(long duration);

    void onCurrentPosition(long currentPosition);

    void onBufferPosition(long bufferPosition);
}
