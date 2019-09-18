package com.effective.android.base.view.list.interfaces

interface IVideoItem: IMediaItem{

    fun onPause()

    fun onResume()

    fun onRelease()
}