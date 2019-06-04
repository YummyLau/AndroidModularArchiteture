package com.effective.android.video

import android.view.View

interface MediaItem {
    fun onSelect()
    fun onUnselected()

    fun onPause()
    fun onResume()
    fun onDestroy()

    fun itemKey(): String
    fun itemPosition(): Int
    fun itemType(): MediaType
    fun mediaView(): View
}