package com.effective.android.video.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Pair
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.ImageView

import com.effective.android.video.R
import com.effective.android.video.bean.VideoCache
import com.effective.android.video.bean.VideoInfo
import com.effective.android.video.bean.VideoStatus
import com.effective.android.video.core.SimplePlayerBaseView
import com.effective.android.video.core.VideoControlLayer
import com.effective.android.video.core.VideoReceivingLayer
import com.effective.android.video.ui.cover.CoverControlLayer
import com.effective.android.video.ui.gesture.GestureControlLayer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout

import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.video_player_view_layout.view.*

class DefaultVideoReceiver @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var isFullScreen: Boolean = false
    private var autoAdjustLayout = true
    private var playerView: SimplePlayerBaseView? = null
    private var playerViewStub: ViewStub? = null

    private var videoControlLayer: VideoControlLayer? = null
    private var videoCache: VideoCache? = null

    val playerViewIfNeed: SimplePlayerBaseView
        get() {
            if (playerView == null) {
                playerView = view_stub_playerView.inflate() as SimplePlayerBaseView
            }
            return playerView!!
        }

    val isPause: Boolean
        get() = if (videoControlLayer != null) {
            videoControlLayer!!.isPause
        } else false

    val isPlaying: Boolean
        get() = if (videoControlLayer != null) {
            videoControlLayer!!.isPlaying
        } else false


    val currentPosition: Long
        get() = if (videoControlLayer != null) {
            videoControlLayer!!.currentPosition
        } else 0

    val duration: Long
        get() = if (videoControlLayer != null) {
            videoControlLayer!!.duration
        } else 0

    private val coverControlLayerImp = object : CoverControlLayer {

        override val isVideoLoaded: Boolean
            get() = if (videoControlLayer != null) {
                videoControlLayer!!.isVideoLoaded
            } else false

        override val isMediaInfoLoaded: Boolean
            get() = if (videoCache != null) {
                videoCache!!.videoInfo.duration > 0
            } else false

        override val isPlaying: Boolean
            get() = if (videoControlLayer != null) {
                videoControlLayer!!.isPlaying
            } else false

        override val isPause: Boolean
            get() = if (videoControlLayer != null) {
                videoControlLayer!!.isPause
            } else false


        override val videoDuration: Long
            get() = if (videoControlLayer != null) {
                videoControlLayer!!.duration
            } else 0L

        override val mediaDuration: Long
            get() = if (videoCache != null) {
                videoCache!!.videoInfo.duration
            } else 0L

        override val contentPosition: Long
            get() = if (videoControlLayer != null) {
                videoControlLayer!!.contentPosition
            } else 0L

        override val duration: Long
            get() = if (videoControlLayer != null) {
                videoControlLayer!!.duration
            } else 0L

        override val videoCurrentPosition: Long
            get() = if (videoControlLayer != null) {
                videoControlLayer!!.currentPosition
            } else 0L

        override fun isFullScreen(): Boolean {
            return isFullScreen
        }

        override val videoInfo: VideoInfo?
            get() = if (videoCache != null) {
                videoCache!!.videoInfo
            } else null

        override fun enterFullScreen() {
            this@DefaultVideoReceiver.isFullScreen = true
        }

        override fun exitFullScreen() {
            this@DefaultVideoReceiver.isFullScreen = false
        }

        override fun pause() {
            if (videoControlLayer != null) {
                videoControlLayer!!.pause(videoCache!!, true)
            }
        }

        override fun play() {
            if (videoControlLayer != null) {
                videoControlLayer!!.play(videoCache!!)
            }
        }

        override fun seekTo(position: Long) {
            if (videoControlLayer != null) {
                videoControlLayer!!.seekTo(videoCache!!, position)
            }
        }

        override fun makeSurePlay() {

        }
    }

    private val gestureControlLayerImp: GestureControlLayer = object : GestureControlLayer {

        override fun handleSingleTapUp(event: MotionEvent): Boolean {
            return true
        }

        override fun handleDoubleTap(event: MotionEvent): Boolean {
            return true
        }

        override fun handleModifyProgress(position: Long) {
            if (videoControlLayer != null) {
                videoControlLayer!!.seekTo(videoCache!!, position)
            }
        }


        override fun getDuration(): Long {
            return if (videoControlLayer != null) {
                videoControlLayer!!.duration
            } else 0L
        }

        override fun getPosition(): Long {
            return if (videoControlLayer != null) {
                videoControlLayer!!.currentPosition
            } else 0L
        }

        override fun onContinueEvent() {
            if (cover_view.isControlVisible) {
                cover_view.hideControlView()
            }
        }

        override fun isVideoPrepare(): Boolean {
            return if (videoControlLayer != null) {
                videoControlLayer!!.isVideoLoaded
            } else false
        }
    }

    /**
     * 相应播放器的事件
     */
    private val videoReceivingLayer = object : VideoReceivingLayer {

        override fun attachedControlLayer(controlLayer: VideoControlLayer) {
            this@DefaultVideoReceiver.videoControlLayer = videoControlLayer
        }

        override fun releaseControlLayer() {
            this@DefaultVideoReceiver.videoControlLayer = null
            cover_view.resetCover()
            cover_view.removeControlRunnable()
            cover_view.removeTrackerProgressRunnable()
        }

        override fun onVideoStatus(status: VideoStatus, message: String?) {
            cover_view.onVideoStatus(status, message!!)
        }

        override fun attachPlayer(player: ExoPlayer) {
            playerViewIfNeed.player = player
            cover_view.postHideControlRunnable()
            cover_view.postTrackerProgressRunnable()
        }

        override fun detachPlayer(player: ExoPlayer) {
            cover_view.resetCover()
            playerViewIfNeed.player = null
            cover_view.removeControlRunnable()
            cover_view.removeTrackerProgressRunnable()
        }

        override fun onBufferPosition(bufferedPosition: Long) {
            cover_view.notifyBufferProcess(bufferedPosition)
            cover_view.setBufferedProgress(cover_view.timeToProgress(bufferedPosition))
        }

        override fun onFirstInit() {
            //第一次初始化，显示视频信息，隐藏cover
            cover_view.setCoverVisible(false)
            cover_view.initControlView()
        }

        override fun clickToPlay() {
            cover_view.setPlayImage(ContextCompat.getDrawable(getContext(), R.drawable.video_ic_pause)!!)
        }

        override fun clickToPause() {
            cover_view.setPlayImage(ContextCompat.getDrawable(getContext(), R.drawable.video_ic_play)!!)
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.video_player_view_layout, this, true)
        gesture_view.initPlayerGesture(gestureControlLayerImp)
        cover_view.initPlayerCover(coverControlLayerImp)
    }


    fun setVideoCache(videoCache: VideoCache) {
        this.videoCache = videoCache
    }


    fun setShutterBackgroundColor(@ColorInt color: Int) {
        playerViewIfNeed.setShutterBackgroundColor(color)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 加载浮层信息
     *
     * @param videoInfo
     * @param scaleType
     * @param fullScreen
     */
    @JvmOverloads
    fun initVideoInfo(videoInfo: VideoInfo, scaleType: ImageView.ScaleType? = null, fullScreen: Boolean = false) {
        cover_view.initCover(videoInfo, scaleType, fullScreen)
    }


    fun seekTo(position: Long) {
        if (videoControlLayer != null) {
            videoControlLayer!!.seekTo(videoCache!!, position)
        }
    }

    fun stop() {
        if (videoControlLayer != null) {
            videoControlLayer!!.stop(videoCache!!)
        }
    }

    fun play() {
        if (videoControlLayer != null) {
            videoControlLayer!!.play(videoCache!!)
        }
    }

    fun pause() {
        if (videoControlLayer != null) {
            videoControlLayer!!.pause(videoCache!!, false)
        }
    }

    fun pause(userAction: Boolean) {
        if (videoControlLayer != null) {
            videoControlLayer!!.pause(videoCache!!, userAction)
        }
    }


    fun replay() {
        if (videoControlLayer != null) {
            videoControlLayer!!.replay(videoCache!!)
        }
    }

    fun release() {
        if (videoControlLayer != null) {
            videoControlLayer!!.release(videoCache!!)
        }
    }

    fun restore() {
        if (videoControlLayer != null) {
            videoControlLayer!!.restore(videoCache!!)
        }
    }


    /**
     * @param visibility
     */
    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        //如果当前变为可见，则需要继续播放视频
        if (visibility == View.VISIBLE) {
            restore()
        } else {
            pause()
        }
    }


    fun adjustPlayerViewVoluntary(containerWidth: Int, containerHeight: Int, videoWidth: Int, videoHeight: Int): Pair<Int, Int> {
        autoAdjustLayout = false
        return adjustPlayerView(containerWidth, containerHeight, videoWidth, videoHeight)
    }

    private fun adjustPlayerView(containerWidth: Int, containerHeight: Int, videoWidth: Int, videoHeight: Int): Pair<Int, Int> {
        var videoWidth = videoWidth
        var videoHeight = videoHeight
        //调整视频布局
        layoutParams.width = containerWidth
        layoutParams.height = containerHeight
        requestLayout()
        if (videoWidth == 0 || videoHeight == 0) {
            videoWidth = containerWidth
            videoHeight = containerHeight
        }
        val videoRadio = videoWidth * 1.0f / videoHeight                 //计算视频比例
        val viewVideoXRadio = containerWidth * 1.0f / videoWidth         //x轴上 布局与视频比
        val videoShowX: Float
        val videoShowY: Float

        if (videoRadio >= 1) {
            //1. 横屏视频优先适配x轴，y轴溢出等比缩放x轴以适配填满y轴
            if (videoHeight * viewVideoXRadio <= containerHeight) {
                setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT)
                videoShowX = containerWidth.toFloat()
                videoShowY = videoShowX / videoRadio
            } else {
                setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT)
                videoShowY = containerHeight.toFloat()
                videoShowX = videoShowY * videoRadio
            }
        } else {
            //2. 视频视频优先适配y轴，x轴溢出等比缩放y轴以适配填满x轴
            if (videoWidth * viewVideoXRadio <= containerWidth) {
                setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT)
                videoShowY = containerHeight.toFloat()
                videoShowX = videoShowY * videoRadio
            } else {
                setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT)
                videoShowX = containerWidth.toFloat()
                videoShowY = videoShowX / videoRadio
            }
        }
        return Pair(videoShowX.toInt(), videoShowY.toInt())
    }

    fun setResizeMode(@AspectRatioFrameLayout.ResizeMode resizeMode: Int) {
        playerViewIfNeed.resizeMode = resizeMode
    }

}
