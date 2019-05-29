package com.effective.android.video.core;

import android.content.Context;
import android.os.Looper;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsCollector;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.Util;

import androidx.annotation.Nullable;

public class SimpleExoPlayerFixMute extends SimpleExoPlayer {

    public static final float VOLUME_MUTE_1 = 0f;

    public static final float VOLUME_MUTE_2 = 0.00618f;

    private static @Nullable
    BandwidthMeter singletonBandwidthMeter;

    protected SimpleExoPlayerFixMute(Context context, RenderersFactory renderersFactory, TrackSelector trackSelector, LoadControl loadControl, BandwidthMeter bandwidthMeter, @Nullable DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, Looper looper) {
        super(context, renderersFactory, trackSelector, loadControl, bandwidthMeter, drmSessionManager, looper);
    }

    protected SimpleExoPlayerFixMute(Context context, RenderersFactory renderersFactory, TrackSelector trackSelector, LoadControl loadControl, @Nullable DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, BandwidthMeter bandwidthMeter, AnalyticsCollector.Factory analyticsCollectorFactory, Looper looper) {
        super(context, renderersFactory, trackSelector, loadControl, drmSessionManager, bandwidthMeter, analyticsCollectorFactory, looper);
    }

    protected SimpleExoPlayerFixMute(Context context, RenderersFactory renderersFactory, TrackSelector trackSelector, LoadControl loadControl, @Nullable DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, BandwidthMeter bandwidthMeter, AnalyticsCollector.Factory analyticsCollectorFactory, Clock clock, Looper looper) {
        super(context, renderersFactory, trackSelector, loadControl, drmSessionManager, bandwidthMeter, analyticsCollectorFactory, clock, looper);
    }

    public static SimpleExoPlayerFixMute make(Context context, LoadControl loadControl) {
        RenderersFactory renderersFactory = new DefaultRenderersFactory(context);
        BandwidthMeter bandwidthMeter = getDefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        return new SimpleExoPlayerFixMute(context, renderersFactory, trackSelector, loadControl, bandwidthMeter, null, Util.getLooper());
    }

    @Override
    public void setPlayWhenReady(boolean playWhenReady) {
        if (getVolume() == VOLUME_MUTE_1) {
            setVolume(VOLUME_MUTE_2);
            setVolume(VOLUME_MUTE_1);
        }
        super.setPlayWhenReady(playWhenReady);
    }

    private static synchronized BandwidthMeter getDefaultBandwidthMeter() {
        if (singletonBandwidthMeter == null) {
            singletonBandwidthMeter = new DefaultBandwidthMeter.Builder().build();
        }
        return singletonBandwidthMeter;
    }
}
