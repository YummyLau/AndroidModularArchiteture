package com.effective.android.component.tab.recommendation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.effective.android.component.square.bean.Article
import com.effective.android.component.tab.recommendation.Sdks
import com.effective.android.component.tab.recommendation.view.CardView
import skin.support.widget.SkinCompatSupportable


class RecommendCard(private val view: CardView) : RecyclerView.ViewHolder(view) {

    fun bindData(data: Article, position: Int) {
        view.bindData(data, position)
    }
}