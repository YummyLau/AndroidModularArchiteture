package com.effective.android.component.blog.view.adapter

import android.view.ViewGroup
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.base.view.list.MediaAdapter
import com.effective.android.base.view.list.MediaHolder
import com.effective.android.component.blog.Constants
import com.effective.android.component.blog.R
import com.effective.android.component.blog.view.adapter.holder.ArticleHolder
import com.effective.android.component.blog.view.adapter.holder.BannerHolder

class ArticleAdapter(dataList: MutableList<IMediaItem>? = null) : MediaAdapter<IMediaItem>(dataList) {

    override fun onCreateVHolder(parent: ViewGroup, viewType: Int): MediaHolder<*> {
        return when (viewType) {
            Constants.articleType -> ArticleHolder(parent, R.layout.cblog_holder_article_layout)
            Constants.bannerType -> BannerHolder(parent, R.layout.cblog_holder_article_layout)
            else -> ArticleHolder(parent, R.layout.cblog_holder_article_layout)
        }
    }
}