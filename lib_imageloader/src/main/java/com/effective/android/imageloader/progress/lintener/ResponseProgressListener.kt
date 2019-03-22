package com.effective.android.imageloader.progress.lintener

import okhttp3.HttpUrl

/**
 * 进度回调，response收集
 */
interface ResponseProgressListener {
    fun update(url: HttpUrl, bytesRead: Long, contentLength: Long)
}
