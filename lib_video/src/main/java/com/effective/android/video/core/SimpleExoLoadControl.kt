package com.effective.android.video.core

import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.Renderer
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.Allocator
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.PriorityTaskManager
import com.google.android.exoplayer2.util.Util

/**
 * created by yummylau on 2019/04/20
 * 播放器缓存控制器
 */
class SimpleExoLoadControl @JvmOverloads constructor(
        private val allocator: DefaultAllocator = DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE),
        minBufferMs: Int = DEFAULT_MIN_BUFFER_MS,
        maxBufferMs: Int = DEFAULT_MAX_BUFFER_MS,
        bufferForPlaybackMs: Int = DEFAULT_BUFFER_FOR_PLAYBACK_MS,
        bufferForPlaybackAfterRebufferMs: Int = DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS,
        private val targetBufferBytesOverwrite: Int = DEFAULT_TARGET_BUFFER_BYTES,
        private val prioritizeTimeOverSizeThresholds: Boolean = DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS, private val priorityTaskManager: PriorityTaskManager? = null) : LoadControl {

    private val minBufferUs: Long
    private val maxBufferUs: Long
    private val bufferForPlaybackUs: Long
    private val bufferForPlaybackAfterRebufferUs: Long

    private var targetBufferSize: Int = 0
    private var isBuffering: Boolean = false

    init {
        assertGreaterOrEqual(bufferForPlaybackMs, 0, "bufferForPlaybackMs", "0")
        assertGreaterOrEqual(
                bufferForPlaybackAfterRebufferMs, 0, "bufferForPlaybackAfterRebufferMs", "0")
        assertGreaterOrEqual(minBufferMs, bufferForPlaybackMs, "minBufferMs", "bufferForPlaybackMs")
        assertGreaterOrEqual(
                minBufferMs,
                bufferForPlaybackAfterRebufferMs,
                "minBufferMs",
                "bufferForPlaybackAfterRebufferMs")
        assertGreaterOrEqual(maxBufferMs, minBufferMs, "maxBufferMs", "minBufferMs")
        minBufferUs = minBufferMs * 1000L
        maxBufferUs = maxBufferMs * 1000L
        bufferForPlaybackUs = bufferForPlaybackMs * 1000L
        bufferForPlaybackAfterRebufferUs = bufferForPlaybackAfterRebufferMs * 1000L
    }


    override fun onPrepared() {
        reset(false)
    }

    override fun onTracksSelected(renderers: Array<Renderer>, trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
        targetBufferSize = if (targetBufferBytesOverwrite == C.LENGTH_UNSET)
            calculateTargetBufferSize(renderers, trackSelections)
        else
            targetBufferBytesOverwrite
        allocator.setTargetBufferSize(targetBufferSize)
    }

    override fun onStopped() {
        reset(true)
    }

    override fun onReleased() {
        reset(true)
    }

    override fun getAllocator(): Allocator {
        return allocator
    }

    override fun getBackBufferDurationUs(): Long {
        return 0
    }

    override fun retainBackBufferFromKeyframe(): Boolean {
        return false
    }

    override fun shouldContinueLoading(bufferedDurationUs: Long, playbackSpeed: Float): Boolean {
        val targetBufferSizeReached = allocator.totalBytesAllocated >= targetBufferSize
        val wasBuffering = isBuffering
        var minBufferUs = this.minBufferUs
        if (playbackSpeed > 1) {
            // The playback speed is faster than real time, so scale up the minimum required medias
            // duration to keep enough medias buffered for a playout duration of minBufferUs.
            val mediaDurationMinBufferUs = Util.getMediaDurationForPlayoutDuration(minBufferUs, playbackSpeed)
            minBufferUs = Math.min(mediaDurationMinBufferUs, maxBufferUs)
        }

        if (bufferedDurationUs < minBufferUs) {
            isBuffering = prioritizeTimeOverSizeThresholds || !targetBufferSizeReached
        } else if (bufferedDurationUs > maxBufferUs || targetBufferSizeReached) {
            isBuffering = false
        } // Else don't change the buffering state
        if (priorityTaskManager != null && isBuffering != wasBuffering) {
            if (isBuffering) {
                priorityTaskManager.add(C.PRIORITY_PLAYBACK)
            } else {
                priorityTaskManager.remove(C.PRIORITY_PLAYBACK)
            }
        }
        return isBuffering && needBuffering()
    }

    fun needBuffering(): Boolean {
        return true
    }

    override fun shouldStartPlayback(bufferedDurationUs: Long, playbackSpeed: Float, rebuffering: Boolean): Boolean {
        var bufferedDurationUs  = Util.getPlayoutDurationForMediaDuration(bufferedDurationUs, playbackSpeed)
        val minBufferDurationUs = if (rebuffering) bufferForPlaybackAfterRebufferUs else bufferForPlaybackUs
        return (minBufferDurationUs <= 0
                || bufferedDurationUs >= minBufferDurationUs
                || !prioritizeTimeOverSizeThresholds && allocator.totalBytesAllocated >= targetBufferSize)
    }

    protected fun calculateTargetBufferSize(
            renderers: Array<Renderer>, trackSelectionArray: TrackSelectionArray): Int {
        var targetBufferSize = 0
        for (i in renderers.indices) {
            if (trackSelectionArray.get(i) != null) {
                targetBufferSize += Util.getDefaultBufferSize(renderers[i].trackType)
            }
        }
        return targetBufferSize
    }


    private fun reset(resetAllocator: Boolean) {
        targetBufferSize = 0
        if (priorityTaskManager != null && isBuffering) {
            priorityTaskManager.remove(C.PRIORITY_PLAYBACK)
        }
        isBuffering = false
        if (resetAllocator) {
            allocator.reset()
        }
    }

    companion object {

        /**
         * 缓冲器低于 DEFAULT_MIN_BUFFER_MS 就开始缓冲，直到 DEFAULT_MAX_BUFFER_MS
         * 。如果缓冲的进度低于观看的进度，当缓存消耗到 DEFAULT_BUFFER_FOR_PLAYBACK_MS
         * 就会停止卡住，直到缓冲器回到 DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
         * 才开始播放
         */
        val DEFAULT_MIN_BUFFER_MS = 4000
        val DEFAULT_MAX_BUFFER_MS = 7000
        val DEFAULT_BUFFER_FOR_PLAYBACK_MS = 1000
        val DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = 3000
        val DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS = true
        val DEFAULT_TARGET_BUFFER_BYTES = C.LENGTH_UNSET

        private fun assertGreaterOrEqual(value1: Int, value2: Int, name1: String, name2: String) {
            Assertions.checkArgument(value1 >= value2, "$name1 cannot be less than $name2")
        }
    }
}
