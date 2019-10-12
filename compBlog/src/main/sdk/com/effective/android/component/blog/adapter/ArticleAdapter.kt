package com.effective.android.component.blog.adapter

import androidx.recyclerview.widget.RecyclerView

interface ArticleAdapter<T> {

    fun updateData(mutableList: MutableList<T>)

    fun replaceData(mutableList: MutableList<T>)

    fun getAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>

    fun getData(): MutableList<T>
}