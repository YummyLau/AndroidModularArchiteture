package com.effective.android.video.core

import android.content.Context
import android.os.Looper

import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsCollector
import com.google.android.exoplayer2.drm.DrmSessionManager
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.util.Clock
import com.google.android.exoplayer2.util.Util

class SimpleExoPlayerFixMute : SimpleExoPlayer {

    protected constructor(context: Context, renderersFactory: RenderersFactory, trackSelector: TrackSelector, loadControl: LoadControl, bandwidthMeter: BandwidthMeter, drmSessionManager: DrmSessionManager<FrameworkMediaCrypto>?, looper: Looper) : super(context, renderersFactory, trackSelector, loadControl, bandwidthMeter, drmSessionManager, looper) {}

    protected constructor(context: Context, renderersFactory: RenderersFactory, trackSelector: TrackSelector, loadControl: LoadControl, drmSessionManager: DrmSessionManager<FrameworkMediaCrypto>?, bandwidthMeter: BandwidthMeter, analyticsCollectorFactory: AnalyticsCollector.Factory, looper: Looper) : super(context, renderersFactory, trackSelector, loadControl, drmSessionManager, bandwidthMeter, analyticsCollectorFactory, looper) {}

    protected constructor(context: Context, renderersFactory: RenderersFactory, trackSelector: TrackSelector, loadControl: LoadControl, drmSessionManager: DrmSessionManager<FrameworkMediaCrypto>?, bandwidthMeter: BandwidthMeter, analyticsCollectorFactory: AnalyticsCollector.Factory, clock: Clock, looper: Looper) : super(context, renderersFactory, trackSelector, loadControl, drmSessionManager, bandwidthMeter, analyticsCollectorFactory, clock, looper) {}

    override fun setPlayWhenReady(playWhenReady: Boolean) {
        if (volume == VOLUME_MUTE_1) {
            volume = VOLUME_MUTE_2
            volume = VOLUME_MUTE_1
        }
        super.setPlayWhenReady(playWhenReady)
    }

    companion object {

        val VOLUME_MUTE_1 = 0f

        val VOLUME_MUTE_2 = 0.00618f

        private var singletonBandwidthMeter: BandwidthMeter? = null

        fun make(context: Context, loadControl: LoadControl): SimpleExoPlayerFixMute {
            val renderersFactory = DefaultRenderersFactory(context)
            val bandwidthMeter = defaultBandwidthMeter
            val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
            val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
            return SimpleExoPlayerFixMute(context, renderersFactory, trackSelector, loadControl, bandwidthMeter, null, Util.getLooper())
        }

        private val defaultBandwidthMeter: BandwidthMeter
            @Synchronized get() {
                if (singletonBandwidthMeter == null) {
                    singletonBandwidthMeter = DefaultBandwidthMeter.Builder().build()
                }
                return singletonBandwidthMeter!!
            }
    }
}
