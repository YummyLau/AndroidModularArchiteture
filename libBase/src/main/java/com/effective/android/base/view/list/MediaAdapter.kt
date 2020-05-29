package com.effective.android.base.view.list

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList

abstract class MediaAdapter<T : IMediaItem>(dataList: MutableList<T>?) : RecyclerView.Adapter<MediaHolder<T>>() {

    var dataList: MutableList<T>

    init {
        var dataList = dataList
        if (dataList == null) {
            dataList = mutableListOf()
        }
        this.dataList = dataList
    }

    @CallSuper
    override fun onBindViewHolder(holder: MediaHolder<T>, position: Int) {
        holder.bindData(this, dataList[position], position, null!!)
    }

    @CallSuper
    override fun onBindViewHolder(holder: MediaHolder<T>, position: Int, payloads: List<Any>) {
        holder.bindData(this, dataList[position], position, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].getViewType()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder<T> {
        return onCreateVHolder(parent, viewType) as MediaHolder<T>
    }

    abstract fun onCreateVHolder(parent: ViewGroup, viewType: Int): MediaHolder<*>

    /**
     * 全量替换
     */
    fun replace(newDataList: List<T>) {
        dataList.clear()
        dataList.addAll(newDataList)
        notifyDataSetChanged()
    }

    fun update(newDataList: List<T>) {
        var olderData: MutableList<T> = mutableListOf()
        olderData.addAll(dataList)
        dataList.clear()
        dataList.addAll(newDataList)
        val diffResult = DiffUtil.calculateDiff(DiffImpl<T>(olderData, dataList), true)
        diffResult.dispatchUpdatesTo(this)
    }

    private class DiffImpl<T : IMediaItem>(private val oldDataList: List<T>, private val newDataList: List<T>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldDataList.size

        override fun getNewListSize(): Int = newDataList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            var oldItem = oldDataList[oldItemPosition]
            var newItem = newDataList[oldItemPosition]
            return if(oldItem.javaClass == newItem.javaClass){
                oldItem.isIdSame(newItem)
            }else{
                false
            }
        }

        /**
         * areItemsTheSame 返回 true 时调用
         */
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            var oldItem = oldDataList[oldItemPosition]
            var newItem = newDataList[oldItemPosition]
            return oldItem.isContentSame(newItem)

        }

        /**
         * areContentsTheSame 返回 false 时调用
         */
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            var oldItem = oldDataList[oldItemPosition]
            var newItem = newDataList[oldItemPosition]
            val payload = Bundle()
            payload.putBoolean("diff_update_media", true)
            return payload
        }
    }
}




