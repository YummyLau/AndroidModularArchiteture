package com.effective.android.component.blog.bean

import com.effective.android.base.view.list.IMediaItem
import com.effective.android.component.blog.Constants

class BannerList : ArrayList<Banner>(), IMediaItem {

    override fun getItemType(): Int = Constants.bannerType

    override fun getViewType(): Int = Constants.bannerType
    override fun replace(item: IMediaItem) {
        if (item is BannerList) {
            clear()
            addAll(item)
        }
    }

    override fun isContentSame(item: IMediaItem): Boolean = false

    override fun isIdSame(item: IMediaItem): Boolean = false
}