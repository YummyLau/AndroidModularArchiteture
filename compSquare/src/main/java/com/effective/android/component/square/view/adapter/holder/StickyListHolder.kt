package com.effective.android.component.square.view.adapter.holder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.effective.android.base.util.StringUtils
import com.effective.android.base.view.list.MediaHolder
import com.effective.android.component.square.R
import com.effective.android.component.square.Sdks
import com.effective.android.component.square.bean.BlogArticle
import com.effective.android.component.square.bean.StickyList
import kotlinx.android.synthetic.main.square_holder_article_layout.view.title
import kotlinx.android.synthetic.main.square_holder_sticky_list_layout.view.*

class StickyListHolder(parent: ViewGroup, layoutId: Int) : MediaHolder<StickyList>(parent, layoutId) {

    override fun bindData(data: StickyList, position: Int, payloads: List<Any>) {
        itemView.stickList.layoutManager = LinearLayoutManager(context)
        itemView.stickList.adapter = StickyAdapter(context, data)
    }

    inner class StickyAdapter(private val context: Context, private val articles: MutableList<BlogArticle>) : RecyclerView.Adapter<StickyHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickyHolder = StickyHolder(LayoutInflater.from(context).inflate(R.layout.square_holder_sticky_layout, parent, false))

        override fun getItemCount(): Int = articles.size

        override fun onBindViewHolder(holder: StickyHolder, position: Int) = holder.bindData(articles[position])
    }

    inner class StickyHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bindData(article: BlogArticle) {
            view.title.text = StringUtils.filterSpecialChat(article.title) ?: ""
            view.setOnClickListener {
                Sdks.getSdk().gotoDetailActivity(context, article)
            }
        }
    }
}
