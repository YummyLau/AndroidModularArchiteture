package com.effective.android.video

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MediaList : RecyclerView {

    var mediaScrollListener: MediaScrollListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        layoutManager = LinearLayoutManager(context)
        mediaScrollListener = MediaScrollListener(this)
        addOnScrollListener(mediaScrollListener!!)
    }


    fun onResume() {
        mediaScrollListener?.syncResume()
    }

    fun onPause() {
        mediaScrollListener?.syncPause()
    }

    fun onDestroy() {
        mediaScrollListener?.syncDestroy()
    }


    @CallSuper
    override fun onChildAttachedToWindow(child: View) {
        super.onChildAttachedToWindow(child)
        mediaScrollListener?.syncChildAttachedToWindow(child)
    }

    override fun onChildDetachedFromWindow(child: View) {
        super.onChildDetachedFromWindow(child)
        mediaScrollListener?.syncChildDetachedFromWindow(child)
    }
}