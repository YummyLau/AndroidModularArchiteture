package com.effective.android.component.square.view.adapter.holder

import android.text.Html
import android.text.Selection
import android.text.Spannable
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.effective.android.base.util.ResourceUtils
import com.effective.android.base.util.StringUtils
import com.effective.android.base.view.Action
import com.effective.android.base.view.Callback
import com.effective.android.base.view.SuperMovementMethod
import com.effective.android.base.view.list.MediaHolder
import com.effective.android.component.square.R
import com.effective.android.component.square.Sdks
import com.effective.android.component.square.bean.BlogArticle
import kotlinx.android.synthetic.main.square_holder_article_layout.view.*
import kotlinx.android.synthetic.main.square_holder_article_layout.view.content
import kotlinx.android.synthetic.main.square_holder_article_layout.view.title
import kotlinx.android.synthetic.main.square_holder_wechat_article_layout.view.*

class WeChatArticleHolder(parent: ViewGroup, layoutId: Int) : MediaHolder<BlogArticle>(parent, layoutId) {

    override fun bindData(data: BlogArticle, position: Int, payloads: List<Any>) {

        itemView.title.text = ResourceUtils.formatString(context, R.string.square_card_title_append, StringUtils.filterSpecialChat(data.title)
                ?: "--")
        val desc = StringUtils.filterSpecialChat(data.desc) ?: ""
        itemView.content.visibility = if (TextUtils.isEmpty(desc)) {
            View.GONE
        } else {
            val callback = object : Callback {
                override fun onAction(action: Action) {
                    when (action) {
                        Action.EXPAND -> {
                            itemView.content.setDefaultText(desc)
                        }
                    }
                }
            }
            itemView.content.setText(desc, false, callback)
            itemView.content.movementMethod = SuperMovementMethod()
            itemView.post {
                Selection.setSelection(itemView.content.text as Spannable,0,1)
            }
            View.VISIBLE
        }
        itemView.createTime.text = String.format(ResourceUtils.getString(context,R.string.square_card_create_time_prefix),data.niceDate)
        itemView.setOnClickListener {
            Sdks.getSdk().gotoDetailActivity(context, data)
        }
    }
}