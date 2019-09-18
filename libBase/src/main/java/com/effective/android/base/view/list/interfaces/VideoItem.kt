package com.effective.android.base.view.list.interfaces

interface VideoItem: MediaItem{

    fun onPause()

    fun onResume()

    fun onRelease()
}