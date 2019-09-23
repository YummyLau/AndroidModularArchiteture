package com.effective.android.component.paccounts.view.adapter

import android.view.ViewGroup
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.base.view.list.MediaAdapter
import com.effective.android.base.view.list.MediaHolder
import com.effective.android.component.paccounts.R

class ArticleAdapter(dataList: MutableList<IMediaItem>? = null) : MediaAdapter<IMediaItem>(dataList) {

    override fun onCreateVHolder(parent: ViewGroup, viewType: Int): MediaHolder<*> {
        return ArticleHolder(parent, R.layout.paccounts_holder_article_layout)
    }
}