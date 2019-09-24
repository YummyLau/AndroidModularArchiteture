package com.effective.android.component.blog.view.adapter

import android.view.ViewGroup
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.base.view.list.MediaHolder
import com.effective.android.component.blog.Constants
import com.effective.android.component.blog.R
import com.effective.android.component.blog.view.adapter.holder.BannerHolder
import com.effective.android.service.kit.view.ArticleAdapter

class BlogArticleAdapter(dataList: MutableList<IMediaItem>? = null) : ArticleAdapter(dataList) {

    override fun onCreateVHolder(parent: ViewGroup, viewType: Int): MediaHolder<*> {
        return when (viewType) {
            Constants.bannerType -> BannerHolder(parent, R.layout.blog_holder_banner_layout)
            else -> super.onCreateVHolder(parent, viewType)
        }
    }
}