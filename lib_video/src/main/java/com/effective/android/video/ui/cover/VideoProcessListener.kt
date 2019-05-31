package com.effective.android.video.ui.cover

interface VideoProcessListener {

    fun onDuration(duration: Long)

    fun onCurrentPosition(currentPosition: Long)

    fun onBufferPosition(bufferPosition: Long)
}
