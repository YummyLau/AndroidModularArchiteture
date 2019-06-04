package com.effective.android.video

import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import java.util.*

class MediaScrollListener(list: MediaList) : RecyclerView.OnScrollListener() {

    private val mediaList = list
    private val attachMediaItems = LinkedList<MediaItem>()
    private var selectedItem: MediaItem? = null

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (recyclerView.scrollState === SCROLL_STATE_IDLE) {
            val validItems = calValidMediaItem(attachMediaItems)
            if (validItems.first.isEmpty()) {
                //如果当前存在选择项，则置空
                if (selectedItem != null) {
                    selectedItem!!.onUnselected()
                    selectedItem = null
                }
            } else {
                val containSelectedItem = containMediaItem(validItems.first, selectedItem)
                if (!containSelectedItem) {
                    if (selectedItem != null) {
                        val selectedPosition = selectedItem!!.itemPosition()
                        unselectedItem(selectedItem)
                        var diffPosition = Int.MAX_VALUE
                        for (data in validItems.second) {
                            if (Math.abs(data.itemPosition() - selectedPosition) <= diffPosition) {
                                selectedItem = data
                            }
                        }
                    } else {
                        selectedItem = validItems.first[0]
                    }
                    selectedItem(selectedItem)
                }
            }
        }
    }


    private fun calValidMediaItem(attachHolders: LinkedList<MediaItem>): Pair<List<MediaItem>, List<MediaItem>> {
        val parentRect = Rect()
        mediaList.getGlobalVisibleRect(parentRect)
        val result = LinkedList<MediaItem>()
        val cabdudate = LinkedList<MediaItem>()
        var bestWeight = 0f
        for (i in attachHolders.indices.reversed()) {
            val rect = Rect()
            //获取在界面视图坐标系中偏移量，获取到的是可视区域
            val measureView = attachHolders[i].mediaView()
            measureView.getGlobalVisibleRect(rect)
            val height = measureView.measuredHeight
            var bottom = rect.bottom
            if (bottom > parentRect.bottom) {
                bottom = parentRect.bottom
            }
            var top = rect.top
            if (top < parentRect.top) {
                top = parentRect.top
            }
            val visibleHeight = 2 * (bottom - top)
            if (height <= visibleHeight) {
                val weight = visibleHeight * 1.0f / height
                if (weight > bestWeight) {
                    cabdudate.clear()
                    cabdudate.add(attachHolders[i])
                    bestWeight = weight
                } else if (weight == bestWeight) {
                    cabdudate.add(attachHolders[i])
                }
                result.add(attachHolders[i])
            }
        }
        return Pair(result, cabdudate)
    }

    private fun unselectedItem(mediaItem: MediaItem?) {
        if (mediaItem == null) {
            return
        }
        mediaItem!!.onUnselected()
    }

    private fun selectedItem(mediaItem: MediaItem?) {
        if (mediaItem == null) {
            return
        }
        mediaItem!!.onSelect()
    }

    private fun containMediaItem(mediaItems: List<MediaItem>?, item: MediaItem?): Boolean {
        if (mediaItems == null || mediaItems.isEmpty() || item == null) {
            return false
        }
        for (mediaItem in mediaItems) {
            if (TextUtils.equals(mediaItem.itemKey(), item!!.itemKey())) {
                return true
            }
        }

        return false
    }

    fun syncResume() {
        if (isVideoMedia()) {
            selectedItem!!.onResume()
        }
    }

    fun syncPause() {
        if (isVideoMedia()) {
            selectedItem!!.onPause()
        }
    }

    fun syncDestroy() {
        if (isVideoMedia()) {
            selectedItem!!.onResume()
        }
    }

    private fun isVideoMedia() = selectedItem != null && selectedItem!!.itemType() == MediaType.VIDEO

    fun syncChildAttachedToWindow(child: View) {
        val holder = mediaList.getChildViewHolder(child)
        if (holder is MediaItem) {
            val item = holder as MediaItem
            attachMediaItems.addFirst(item)
        }
    }

    fun syncChildDetachedFromWindow(child: View) {
        val holder = mediaList.getChildViewHolder(child)
        if (holder is MediaItem) {
            val item = holder as MediaItem
            if (selectedItem != null && TextUtils.equals(selectedItem!!.itemKey(), item.itemKey())) {
                unselectedItem(selectedItem)
                selectedItem = null
            }
            attachMediaItems.remove(item)
        }
    }
}
