package com.effective.android.video.ui.gesture

import android.view.MotionEvent

interface GestureControlLayer {

    fun handleSingleTapUp(event: MotionEvent): Boolean

    fun handleDoubleTap(event: MotionEvent): Boolean

    fun handleModifyProgress(position: Long)

    fun getDuration(): Long

    fun getPosition(): Long

    fun onContinueEvent()

    fun isVideoPrepare(): Boolean
}