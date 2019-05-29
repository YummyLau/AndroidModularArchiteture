package com.effective.android.video.core;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import com.google.android.exoplayer2.util.Util;

/**
 * created by yummylau on 2019/04/20
 * 大神
 */
public class SimpleExoLoadControl implements LoadControl {

    /**
     * 缓冲器低于 DEFAULT_MIN_BUFFER_MS 就开始缓冲，直到 DEFAULT_MAX_BUFFER_MS
     * 。如果缓冲的进度低于观看的进度，当缓存消耗到 DEFAULT_BUFFER_FOR_PLAYBACK_MS
     * 就会停止卡住，直到缓冲器回到 DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
     * 才开始播放
     */
    public static final int DEFAULT_MIN_BUFFER_MS = 4000;
    public static final int DEFAULT_MAX_BUFFER_MS = 7000;
    public static final int DEFAULT_BUFFER_FOR_PLAYBACK_MS = 1000;
    public static final int DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = 3000;
    public static final boolean DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS = true;
    public static final int DEFAULT_TARGET_BUFFER_BYTES = C.LENGTH_UNSET;

    private final DefaultAllocator allocator;

    private final long minBufferUs;
    private final long maxBufferUs;
    private final long bufferForPlaybackUs;
    private final long bufferForPlaybackAfterRebufferUs;
    private final int targetBufferBytesOverwrite;
    private final boolean prioritizeTimeOverSizeThresholds;
    private final PriorityTaskManager priorityTaskManager;

    private int targetBufferSize;
    private boolean isBuffering;

    public SimpleExoLoadControl() {
        this(new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE));
    }

    public SimpleExoLoadControl(DefaultAllocator allocator) {
        this(allocator,
                DEFAULT_MIN_BUFFER_MS,
                DEFAULT_MAX_BUFFER_MS,
                DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS,
                DEFAULT_TARGET_BUFFER_BYTES,
                DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS);
    }

    public SimpleExoLoadControl(
            DefaultAllocator allocator,
            int minBufferMs,
            int maxBufferMs,
            int bufferForPlaybackMs,
            int bufferForPlaybackAfterRebufferMs,
            int targetBufferBytes,
            boolean prioritizeTimeOverSizeThresholds) {
        this(allocator, minBufferMs, maxBufferMs, bufferForPlaybackMs,
                bufferForPlaybackAfterRebufferMs, targetBufferBytes, prioritizeTimeOverSizeThresholds, null);
    }

    public SimpleExoLoadControl(
            DefaultAllocator allocator,
            int minBufferMs,
            int maxBufferMs,
            int bufferForPlaybackMs,
            int bufferForPlaybackAfterRebufferMs,
            int targetBufferBytes,
            boolean prioritizeTimeOverSizeThresholds
            , PriorityTaskManager priorityTaskManager) {
        assertGreaterOrEqual(bufferForPlaybackMs, 0, "bufferForPlaybackMs", "0");
        assertGreaterOrEqual(
                bufferForPlaybackAfterRebufferMs, 0, "bufferForPlaybackAfterRebufferMs", "0");
        assertGreaterOrEqual(minBufferMs, bufferForPlaybackMs, "minBufferMs", "bufferForPlaybackMs");
        assertGreaterOrEqual(
                minBufferMs,
                bufferForPlaybackAfterRebufferMs,
                "minBufferMs",
                "bufferForPlaybackAfterRebufferMs");
        assertGreaterOrEqual(maxBufferMs, minBufferMs, "maxBufferMs", "minBufferMs");
        this.allocator = allocator;
        minBufferUs = minBufferMs * 1000L;
        maxBufferUs = maxBufferMs * 1000L;
        bufferForPlaybackUs = bufferForPlaybackMs * 1000L;
        bufferForPlaybackAfterRebufferUs = bufferForPlaybackAfterRebufferMs * 1000L;
        targetBufferBytesOverwrite = targetBufferBytes;
        this.prioritizeTimeOverSizeThresholds = prioritizeTimeOverSizeThresholds;
        this.priorityTaskManager = priorityTaskManager;
    }


    @Override
    public void onPrepared() {
        reset(false);
    }

    @Override
    public void onTracksSelected(Renderer[] renderers, TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        targetBufferSize =
                targetBufferBytesOverwrite == C.LENGTH_UNSET
                        ? calculateTargetBufferSize(renderers, trackSelections)
                        : targetBufferBytesOverwrite;
        allocator.setTargetBufferSize(targetBufferSize);
    }

    @Override
    public void onStopped() {
        reset(true);
    }

    @Override
    public void onReleased() {
        reset(true);
    }

    @Override
    public Allocator getAllocator() {
        return allocator;
    }

    @Override
    public long getBackBufferDurationUs() {
        return 0;
    }

    @Override
    public boolean retainBackBufferFromKeyframe() {
        return false;
    }

    @Override
    public boolean shouldContinueLoading(long bufferedDurationUs, float playbackSpeed) {
        boolean targetBufferSizeReached = allocator.getTotalBytesAllocated() >= targetBufferSize;
        boolean wasBuffering = isBuffering;
        long minBufferUs = this.minBufferUs;
        if (playbackSpeed > 1) {
            // The playback speed is faster than real time, so scale up the minimum required medias
            // duration to keep enough medias buffered for a playout duration of minBufferUs.
            long mediaDurationMinBufferUs =
                    Util.getMediaDurationForPlayoutDuration(minBufferUs, playbackSpeed);
            minBufferUs = Math.min(mediaDurationMinBufferUs, maxBufferUs);
        }

        if (bufferedDurationUs < minBufferUs) {
            isBuffering = prioritizeTimeOverSizeThresholds || !targetBufferSizeReached;
        } else if (bufferedDurationUs > maxBufferUs || targetBufferSizeReached) {
            isBuffering = false;
        } // Else don't change the buffering state
        if (priorityTaskManager != null && isBuffering != wasBuffering) {
            if (isBuffering) {
                priorityTaskManager.add(C.PRIORITY_PLAYBACK);
            } else {
                priorityTaskManager.remove(C.PRIORITY_PLAYBACK);
            }
        }
        return isBuffering && needBuffering();
    }

    public boolean needBuffering() {
        return true;
    }

    @Override
    public boolean shouldStartPlayback(long bufferedDurationUs, float playbackSpeed, boolean rebuffering) {
        bufferedDurationUs = Util.getPlayoutDurationForMediaDuration(bufferedDurationUs, playbackSpeed);
        long minBufferDurationUs = rebuffering ? bufferForPlaybackAfterRebufferUs : bufferForPlaybackUs;
        return minBufferDurationUs <= 0
                || bufferedDurationUs >= minBufferDurationUs
                || (!prioritizeTimeOverSizeThresholds
                && allocator.getTotalBytesAllocated() >= targetBufferSize);
    }

    protected int calculateTargetBufferSize(
            Renderer[] renderers, TrackSelectionArray trackSelectionArray) {
        int targetBufferSize = 0;
        for (int i = 0; i < renderers.length; i++) {
            if (trackSelectionArray.get(i) != null) {
                targetBufferSize += Util.getDefaultBufferSize(renderers[i].getTrackType());
            }
        }
        return targetBufferSize;
    }


    private void reset(boolean resetAllocator) {
        targetBufferSize = 0;
        if (priorityTaskManager != null && isBuffering) {
            priorityTaskManager.remove(C.PRIORITY_PLAYBACK);
        }
        isBuffering = false;
        if (resetAllocator) {
            allocator.reset();
        }
    }

    private static void assertGreaterOrEqual(int value1, int value2, String name1, String name2) {
        Assertions.checkArgument(value1 >= value2, name1 + " cannot be less than " + name2);
    }
}
