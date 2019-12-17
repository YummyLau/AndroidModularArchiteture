package com.effective.android.component.tab.recommendation.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.LruCache
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.effective.android.component.square.bean.Article
import com.effective.android.component.tab.recommendation.view.CardView

class RecommendAdapter(private val context: Context, private val mutableList: MutableList<Article>) : RecyclerView.Adapter<RecommendCard>() {

    private val imageCache = LruCache<String, Bitmap>(12)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendCard = RecommendCard(CardView(imageCache,context))

    override fun getItemCount(): Int = mutableList.size

    override fun onBindViewHolder(holder: RecommendCard, position: Int) {
        holder.bindData(mutableList[position], position)
    }

    fun setData(data: MutableList<Article>) {
        mutableList.clear()
        if (!data.isNullOrEmpty()) {
            mutableList.addAll(data)
        }
        notifyDataSetChanged()
    }
}
