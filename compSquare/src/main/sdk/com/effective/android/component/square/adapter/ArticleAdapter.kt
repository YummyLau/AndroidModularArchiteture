package com.effective.android.component.square.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

interface ArticleAdapter<T> {

    fun updateData(mutableList: MutableList<T>)

    fun replaceData(mutableList: MutableList<T>)

    fun getAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>

    fun getData(): MutableList<T>

    fun getListItemDecoration(context: Context, vertical: Boolean = true): RecyclerView.ItemDecoration
}