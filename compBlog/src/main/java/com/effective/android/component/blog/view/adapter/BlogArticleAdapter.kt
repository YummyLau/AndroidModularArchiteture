package com.effective.android.component.blog.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.effective.android.base.view.list.IMediaItem
import com.effective.android.base.view.list.MediaAdapter
import com.effective.android.base.view.list.MediaHolder
import com.effective.android.component.blog.Constants
import com.effective.android.component.blog.R
import com.effective.android.component.blog.adapter.ArticleAdapter
import com.effective.android.component.blog.bean.Article
import com.effective.android.component.blog.bean.BlogArticle
import com.effective.android.component.blog.view.adapter.holder.ArticleHolder
import com.effective.android.component.blog.view.adapter.holder.BannerHolder

class BlogArticleAdapter(dataList: MutableList<IMediaItem>? = null) : MediaAdapter<IMediaItem>(dataList), ArticleAdapter<Article> {

    override fun onCreateVHolder(parent: ViewGroup, viewType: Int): MediaHolder<*> {
        return when (viewType) {
            Constants.articleType -> ArticleHolder(parent, R.layout.blog_holder_article_layout)
            Constants.bannerType -> BannerHolder(parent, R.layout.blog_holder_banner_layout)
            else -> ArticleHolder(parent, R.layout.blog_holder_article_layout)
        }
    }

    override fun updateData(mutableList: MutableList<Article>) {
        val toList = mutableListOf<IMediaItem>()
        for (article in mutableList) {
            toList.add(BlogArticle(article))
        }
        this.update(toList)
    }

    override fun replaceData(mutableList: MutableList<Article>) {
        val toList = mutableListOf<IMediaItem>()
        for (article in mutableList) {
            toList.add(BlogArticle(article))
        }
        this.replace(toList)
    }

    override fun getData(): MutableList<Article> {
        val toList = mutableListOf<Article>()
        for (media in dataList) {
            if (media is Article) {
                toList.add(BlogArticle(media))
            }
        }
        return toList
    }

    override fun getAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return this as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }
}