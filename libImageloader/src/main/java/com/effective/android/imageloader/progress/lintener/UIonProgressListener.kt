package com.effective.android.imageloader.progress.lintener

/**
 * 进度回调，提供给ui层回调
 */
interface UIonProgressListener {
    fun onProgress(byteRead: Long, expectedLength: Long)

    /**
     * 步长，假如设置为1，则 每次以1%进行更新
     */
    fun getGranualityPercentage(): Float
}