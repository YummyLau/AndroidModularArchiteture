package com.effective.android.base.view.list

interface IMediaItem {

    fun getType(): Int

    fun getViewType(): Int

    fun replace(item: IMediaItem)

    fun isContentSame(item: IMediaItem): Boolean

    fun isIdSame(item: IMediaItem): Boolean

}