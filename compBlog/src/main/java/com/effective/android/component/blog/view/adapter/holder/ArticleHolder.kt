package com.effective.android.component.blog.view.adapter.holder

import android.text.Html
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.effective.android.base.util.StringUtils
import com.effective.android.base.view.list.MediaHolder
import com.effective.android.component.blog.bean.BlogArticle
import kotlinx.android.synthetic.main.blog_holder_article_layout.view.*

class ArticleHolder(parent: ViewGroup, layoutId: Int) : MediaHolder<BlogArticle>(parent, layoutId) {

    override fun bindData(data: BlogArticle, position: Int, payloads: List<Any>) {
        itemView.title.text = data.title
        itemView.content.text = StringUtils.filterSpecialChat(Html.fromHtml(data.desc).toString())
        itemView.content.visibility = if (!TextUtils.isEmpty(data.desc)) View.VISIBLE else View.GONE
        itemView.userInfo.text = data.niceDate + "@" + data.author
        itemView.extra.text = "分类：" + data.superChapterName + "/" + data.chapterName
    }
}
