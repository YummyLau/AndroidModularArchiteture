package com.effective.android.imageloader.progress

import android.os.Handler
import android.os.Looper
import com.effective.android.imageloader.progress.lintener.ResponseProgressListener
import com.effective.android.imageloader.progress.lintener.UIonProgressListener
import okhttp3.HttpUrl
import java.util.*

class DispatchingProgressManager : ResponseProgressListener {

    companion object {
        private val LISTENERS = WeakHashMap<String, UIonProgressListener>()
        private val PROGRESSES = WeakHashMap<String, Long>()

        @JvmStatic
        fun forget(url: String) {
            LISTENERS.remove(url)
            PROGRESSES.remove(url)
        }

        @JvmStatic
        fun expect(url: String, listener: UIonProgressListener) {
            LISTENERS[url] = listener
        }
    }

    private val handler = Handler(Looper.getMainLooper())


    override fun update(url: HttpUrl, bytesRead: Long, contentLength: Long) {
        val key = url.toString()
        val listener = LISTENERS[key]

        if (contentLength <= bytesRead) {
            forget(key)
        }

        if (listener != null && needsDispatch(key, bytesRead, contentLength, listener!!.getGranualityPercentage())) {
            handler.post {
                listener.onProgress(bytesRead, contentLength)
            }
        }
    }

    private fun needsDispatch(key: String, current: Long, total: Long, granularity: Float): Boolean {
        if (granularity == 0f || current == 0L || total == current) {
            return true
        }
        val percent = 100f * current / total
        val currentProgress = (percent / granularity).toLong()
        val lastProgress = PROGRESSES[key]
        return if (lastProgress == null || lastProgress != currentProgress) {
            PROGRESSES[key] = currentProgress
            true
        } else {
            false
        }
    }
}