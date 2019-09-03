package com.effective.android.base.activity.swipeback

interface SwipeBackActivityBase {
    /**
     * @return the SwipeBackLayout associated with this activity.
     */
    fun getSwipeBackLayout(): SwipeBackLayout

    fun setSwipeBackEnable(enable: Boolean)

    /**
     * Scroll out contentView and finish the activity
     */
    fun scrollToFinishActivity()
}
