package com.effective.android.component.square.bean

import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.square.Constants

class StickyList: ArrayList<BlogArticle>(),IMediaItem{

    override fun getItemType(): Int = Constants.stickyType

    override fun getViewType(): Int = Constants.stickyType

    override fun replace(item: IMediaItem) {
        if (item is StickyList) {
            clear()
            addAll(item)
        }
    }

    override fun isContentSame(item: IMediaItem): Boolean = true

    override fun isIdSame(item: IMediaItem): Boolean = true

}