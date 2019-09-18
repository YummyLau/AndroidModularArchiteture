package com.effective.android.base.view.list.listener

import android.util.Log

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING


/**
 * 图片滑动不加载
 * created by yummylau0 on 2018/5/28
 */
class ImageScrollListener(private val recyclerView: RecyclerView, private val resumeRequest: Runnable, private val pauseRequests: Runnable) : RecyclerView.OnScrollListener() {

    init {
        this.recyclerView.addOnScrollListener(this)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        when (newState) {

            SCROLL_STATE_DRAGGING, SCROLL_STATE_IDLE -> {
                resumeRequests()
            }

            SCROLL_STATE_SETTLING -> {
                pauseRequests()
            }
        }
    }

    private fun resumeRequests() {
        Log.d("ImageScrollListener", "resumeRequests")
        try {
            resumeRequest.run()
        } catch (e: Exception) {
            Log.e("ImageScrollListener", e.message)
        }

    }

    private fun pauseRequests() {
        Log.d("ImageScrollListener", "pauseRequests")
        try {
            pauseRequests.run()
        } catch (e: Exception) {
            Log.e("ImageScrollListener", e.message)
        }

    }
}
