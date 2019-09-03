package com.effective.android.base.listener

import android.os.SystemClock
import android.view.View

/**
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
abstract class OnDoubleClickListener : View.OnClickListener {

    private var HITS = LongArray(2)

    override fun onClick(v: View?) {
        System.arraycopy(HITS, 1, HITS, 0, HITS.size - 1)
        HITS[HITS.size - 1] = SystemClock.uptimeMillis()
        val currentTime = SystemClock.uptimeMillis()
        if (HITS[0] >= currentTime - 500) {
            onDoubleClick(v!!)
        }
    }

    abstract fun onDoubleClick(v: View)
}