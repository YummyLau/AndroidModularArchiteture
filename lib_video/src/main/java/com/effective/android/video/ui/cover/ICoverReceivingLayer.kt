package com.effective.android.video.ui.cover

/**
 * 浮层ui层变化
 * Created by yummylau on 2019/04/20.
 */
interface ICoverReceivingLayer {

    fun onCoverClick()

    fun onControlVisible(visible: Boolean)

    fun onBack(): Boolean

    fun onFullScreen(unfold: Boolean): Boolean
}
