package com.effective.android.base.view.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class MediaHolder<T : IMediaItem>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var context: Context = itemView.context
    lateinit var adapter: MediaAdapter<*>

    constructor(parent: ViewGroup, layoutId: Int) : this(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    fun bindData(adapter: MediaAdapter<*>, data: T, position: Int, payloads: List<Any>) {
        this.adapter = adapter
        bindData(data, position, payloads)
    }

    abstract fun bindData(data: T, position: Int, payloads: List<Any>)
}
