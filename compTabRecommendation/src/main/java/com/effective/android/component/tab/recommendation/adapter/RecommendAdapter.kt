package com.effective.android.component.tab.recommendation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.effective.android.component.square.bean.Article
import com.effective.android.component.tab.recommendation.R

class RecommendAdapter(private val context: Context, private val mutableList: MutableList<Article>) : RecyclerView.Adapter<RecommendCard>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendCard = RecommendCard(LayoutInflater.from(context).inflate(R.layout.tabr_holder_recommend_card_layout, parent, false))

    override fun getItemCount(): Int = mutableList.size

    override fun onBindViewHolder(holder: RecommendCard, position: Int) {
        holder.bindData(mutableList[position], position)
    }

    fun insertDatas(data: MutableList<Article>) {
        if (!mutableList.isNullOrEmpty()) {
            val size = mutableList.size
            val insertSize = data.size
            mutableList.addAll(data)
            notifyItemRangeInserted(size, insertSize)
        }
    }

    fun setData(data: MutableList<Article>) {
        mutableList.clear()
        if (!data.isNullOrEmpty()) {
            mutableList.addAll(data)
        }
        notifyDataSetChanged()
    }
}
