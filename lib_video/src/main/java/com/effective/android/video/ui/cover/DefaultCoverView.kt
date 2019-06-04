package com.effective.android.video.ui.cover

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView

import com.effective.android.video.R
import com.effective.android.video.Utils
import com.effective.android.video.bean.VideoInfo
import com.effective.android.video.bean.VideoStatus

import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.video_default_cover_view_layout.view.*

/**
 * 默认提供的浮层view
 * Created by yummylau on 2019/04/20.
 */
class DefaultCoverView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var playStatusIcon: Drawable? = null
    private var playIcon: Drawable? = null
    private var pauseStatusIcon: Drawable? = null
    private var pauseIcon: Drawable? = null
    private var loadingStatusIcon: Drawable? = null
    private var replayIcon: Drawable? = null
    private var backIcon: Drawable? = null
    private var volumeIcon: Drawable? = null
    private var loadingAni: Animation? = null

    private var enableControl: Boolean = false
    private var enableVideoTime: Boolean = false
    private var enableBack: Boolean = false
    private var enableVolume: Boolean = false

    var isTouchProgress = false
        private set

    private var videoStatus: VideoStatus? = null
    private var controlListener: CoverReceivingLayer? = null
    private var coverControlLayer: CoverControlLayer? = null
    private var statusListener: VideoStatusListener? = null
    private var processListener: VideoProcessListener? = null

    private var innerHideControlRunnable: InnerHideControlRunnable? = null
    private var innerTrackerProgressRunnable: InnerTrackerProgressRunnable? = null

    private var scaleType: ImageView.ScaleType? = null
    private var isFullScreenCover: Boolean = false
    private var videoInfo: VideoInfo? = null

    val isMidStatusImgVisible: Boolean
        get() = control_mid_video_status_img.visibility == View.VISIBLE


    var isControlVisible: Boolean
        get() = control_bottom_layout.visibility == View.VISIBLE
        set(visible) {
            control_bottom_layout!!.visibility = if (visible) View.VISIBLE else View.GONE
            if (coverControlLayer != null && coverControlLayer!!.isPlaying) {
                setMidStatusImg(VideoStatus.PLAYING, visible)
            } else if (coverControlLayer != null && coverControlLayer!!.isPause) {
                setMidStatusImg(VideoStatus.PAUSE, visible)
            }
            removeCallbacks(innerHideControlRunnable)
            if (visible) {
                postHideControlRunnable()
            }
            if (controlListener != null) {
                controlListener!!.onControlVisible(control_bottom_layout!!.visibility == View.VISIBLE)
            }
        }

    private val loadingAnimation: Animation?
        get() {
            if (loadingAni == null) {
                loadingAni = AnimationUtils.loadAnimation(context, R.anim.video_loading_anim)
            }
            return loadingAni
        }

    init {
        initView(context, attrs, defStyleAttr)
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        //顶部
        LayoutInflater.from(context).inflate(R.layout.video_default_cover_view_layout, this, true)

        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DefaultCoverView, defStyleAttr, 0)
        if (typedArray != null) {
            backIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_ic_back)
            volumeIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_ic_volume)
            playStatusIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_ic_play_state)
            pauseStatusIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_ic_pause_state)
            replayIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_ic_replay_state)
            loadingStatusIcon = typedArray.getDrawable(R.styleable.DefaultCoverView_ic_loading_state)
            enableVideoTime = typedArray.getBoolean(R.styleable.DefaultCoverView_open_time, false)
            enableBack = typedArray.getBoolean(R.styleable.DefaultCoverView_open_back, false)
            enableVolume = typedArray.getBoolean(R.styleable.DefaultCoverView_open_volume, false)
            enableControl = typedArray.getBoolean(R.styleable.DefaultCoverView_open_control, false)
            typedArray.recycle()
        }
        playIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_play)
        pauseIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_pause)

        if (playStatusIcon == null) {
            playStatusIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_play_state)
        }
        if (pauseStatusIcon == null) {
            pauseStatusIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_pause_state)
        }
        if (replayIcon == null) {
            replayIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_replay_state)
        }
        if (loadingStatusIcon == null) {
            loadingStatusIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_loading_state)
        }
        if (backIcon == null) {
            backIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_top_left_back)
        }
        if (volumeIcon == null) {
            volumeIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_top_right_volume)
        }

        control_mid_video_status_img.setImageDrawable(loadingStatusIcon)
        control_lt_back.setImageDrawable(backIcon)
        control_rt_volume.setImageDrawable(volumeIcon)

        control_rt_volume.visibility = if (enableVolume) View.VISIBLE else View.GONE
        control_lt_back.visibility = if (enableBack) View.VISIBLE else View.GONE

        cover_layout.visibility = View.GONE

        control_lt_back.setOnClickListener(this)
        control_rt_volume.setOnClickListener(this)
        control_rb_full_screen.setOnClickListener(this)
        control_lb_play.setOnClickListener(this)
        control_mid_video_status_tip.setOnClickListener(this)
        control_mid_video_status_img.setOnClickListener(this)

        if (innerHideControlRunnable == null) {
            innerHideControlRunnable = InnerHideControlRunnable()
        } else {
            removeCallbacks(innerHideControlRunnable)
        }
        if (innerTrackerProgressRunnable == null) {
            innerTrackerProgressRunnable = InnerTrackerProgressRunnable()
        } else {
            removeCallbacks(innerTrackerProgressRunnable)
        }
    }

    fun setEnableVideoTime(enableTime: Boolean) {
        this.enableVideoTime = enableTime
    }

    fun setLoadingStatusIcon(@DrawableRes id: Int) {
        loadingStatusIcon = ContextCompat.getDrawable(context, id)
        control_mid_video_status_img.setImageDrawable(loadingStatusIcon)
    }

    fun setPauseStatusIcon(@DrawableRes id: Int) {
        pauseStatusIcon = ContextCompat.getDrawable(context, id)
    }

    fun setPlayStatusIcon(@DrawableRes id: Int) {
        playStatusIcon = ContextCompat.getDrawable(context, id)
    }

    fun setCoverListener(controlListener: CoverReceivingLayer) {
        this.controlListener = controlListener
    }

    fun initPlayerCover(coverControlLayer: CoverControlLayer) {
        this.coverControlLayer = coverControlLayer
    }

    fun setStatusListener(statusListener: VideoStatusListener) {
        this.statusListener = statusListener
    }

    fun setProcessListener(processListener: VideoProcessListener) {
        this.processListener = processListener
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.control_lt_back -> {
                if (controlListener != null) {
                    controlListener!!.onBack()
                }
            }
            R.id.control_lb_play -> {
                if (coverControlLayer != null && coverControlLayer!!.isPlaying) {
                    isControlVisible = true
                    coverControlLayer!!.pause()
                    return
                }
                if (coverControlLayer != null && coverControlLayer!!.isPause) {
                    isControlVisible = true
                    coverControlLayer!!.play()
                    return
                }
            }

            R.id.control_mid_video_status_img -> {
                if (coverVisible()) {
                    if (controlListener != null) {
                        controlListener!!.onCoverClick()
                    }
                    return
                }

                if (coverControlLayer != null && coverControlLayer!!.isPlaying) {
                    isControlVisible = true
                    coverControlLayer!!.pause()
                    return
                }
                if (coverControlLayer != null && coverControlLayer!!.isPause) {
                    isControlVisible = true
                    coverControlLayer!!.play()
                    return
                }
            }
            R.id.control_rb_full_screen -> {
                if (coverControlLayer != null && coverControlLayer!!.isFullScreen()) {
                    exitFullScreen()
                } else {
                    enterFullScreen()
                }
            }
        }
    }

    fun enterFullScreen() {
        if (controlListener != null && controlListener!!.onFullScreen(true)) {
            if (coverControlLayer != null) {
                coverControlLayer!!.enterFullScreen()
                control_rb_full_screen!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.video_ic_collapse))
            }
        }
    }

    fun exitFullScreen() {
        if (controlListener != null && controlListener!!.onFullScreen(false)) {
            if (coverControlLayer != null) {
                coverControlLayer!!.exitFullScreen()
                control_rb_full_screen!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.video_ic_unfold))
            }
        }
    }

    private fun coverVisible(): Boolean {
        return cover_layout!!.visibility == View.VISIBLE
    }

    fun setCoverVisible(visible: Boolean) {
        cover_layout!!.visibility = if (visible) View.VISIBLE else View.GONE
        if (visible) {
            if (coverControlLayer != null && coverControlLayer!!.isVideoLoaded) {
                setVideoTimeTip(coverControlLayer!!.duration, 0)
            } else if (coverControlLayer != null && coverControlLayer!!.isMediaInfoLoaded) {
                setVideoTimeTip(coverControlLayer!!.mediaDuration, 0)
            } else if (videoInfo != null) {
                setVideoTimeTip(videoInfo!!.duration, 0)
            } else {
                control_rb_video_time!!.visibility = View.GONE
            }
        }
    }

    fun setCoverColor(@DrawableRes resId: Int) {
        cover!!.setImageResource(resId)
    }

    fun setVideoTimeTip(duration: Long, position: Long) {
        if (enableVideoTime) {
            if (duration > position) {
                control_rb_video_time.visibility = View.VISIBLE
                control_rb_video_time.text = Utils.getRemainingVideoTimeStr(duration, position)
            } else {
                control_rb_video_time.visibility = View.GONE
            }
        } else {
            control_rb_video_time.visibility = View.GONE
        }
    }

    private fun setMidStatusImg(videoStatus: VideoStatus, isVisible: Boolean) {
        when (videoStatus) {
            VideoStatus.PREPARE -> {
                if (isVisible) {
                    control_mid_video_status_img!!.clearAnimation()
                    control_mid_video_status_img!!.visibility = View.VISIBLE
                    control_mid_video_status_img!!.setImageDrawable(loadingStatusIcon)
                    control_mid_video_status_img!!.startAnimation(loadingAnimation)
                } else {
                    control_mid_video_status_img!!.clearAnimation()
                    control_mid_video_status_img!!.visibility = View.GONE
                }
            }
            VideoStatus.BUFFERING -> {
                if (isVisible) {
                    control_mid_video_status_img!!.clearAnimation()
                    control_mid_video_status_img!!.visibility = View.VISIBLE
                    control_mid_video_status_img!!.setImageDrawable(loadingStatusIcon)
                    control_mid_video_status_img!!.startAnimation(loadingAnimation)
                } else {
                    control_mid_video_status_img!!.clearAnimation()
                    control_mid_video_status_img!!.visibility = View.GONE
                }
            }

            VideoStatus.PLAYING -> {
                control_mid_video_status_img!!.clearAnimation()
                control_mid_video_status_img!!.setImageDrawable(pauseStatusIcon)
                control_lb_play!!.setImageDrawable(pauseIcon)
                control_mid_video_status_img!!.visibility = if (isVisible) View.VISIBLE else View.GONE
            }

            VideoStatus.PAUSE -> {
                control_mid_video_status_img!!.clearAnimation()
                control_mid_video_status_img!!.setImageDrawable(playStatusIcon)
                control_lb_play!!.setImageDrawable(playIcon)
                control_mid_video_status_img!!.visibility = if (isVisible) View.VISIBLE else View.GONE
            }

            else -> {
                control_mid_video_status_img!!.clearAnimation()
                control_mid_video_status_img!!.visibility = View.GONE
            }
        }
    }

    fun onVideoStatus(status: VideoStatus?, msg: String) {
        when (status) {
            VideoStatus.PREPARE -> {
                setMidStatusImg(VideoStatus.PREPARE, true)                                                  //视频信息加载完成之前，loading显示，界面点击无效
                setMidStatusTip(false)
                isControlVisible = false
            }
            VideoStatus.BUFFERING -> {
                if (videoStatus !== VideoStatus.BUFFERING) {                    //如果当前mid正在显示，则需要更改状态
                    setMidStatusImg(VideoStatus.BUFFERING, true)
                }
                setMidStatusTip(false)
            }
            VideoStatus.PLAYING -> {
                if (isMidStatusImgVisible && isControlVisible && videoStatus !== VideoStatus.PLAYING) {           //如果当前mid正在显示，则需要更改状态
                    setMidStatusImg(status, true)
                } else {
                    setMidStatusImg(status, false)
                }
                setMidStatusTip(false)
            }
            VideoStatus.PAUSE -> {
                if (isMidStatusImgVisible && isControlVisible && videoStatus !== VideoStatus.PAUSE) {               //如果当前mid正在显示，则需要更改状态
                    setMidStatusImg(status, true)
                } else {
                    setMidStatusImg(status, false)
                }
                setMidStatusTip(false)
            }
            VideoStatus.FINISH -> {
                setMidStatusImg(status, false)
                setMidStatusTip(false)
            }
            VideoStatus.ERROR -> {
                setMidStatusImg(status, false)
                setMidStatusTip(false)
            }//                setMidStatusTip(true);
            //                control_mid_video_status_tip.setText(R.string.video_load_error);
            else -> {

            }
        }
        if (statusListener != null) {
            statusListener!!.onStatus(status, msg)
        }
        videoStatus = status
    }


    fun setMidStatusTip(visible: Boolean) {
        control_mid_video_status_tip!!.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun hideControlView() {
        isControlVisible = false
    }

    fun initCover(videoInfo: VideoInfo?, scaleType: ImageView.ScaleType?, fullScreen: Boolean) {
        if (videoInfo == null) {
            setCoverVisible(false)
            control_mid_video_status_img!!.visibility = View.GONE
            return
        }
        this.videoInfo = videoInfo
        setCoverVisible(true)
        control_mid_video_status_img!!.clearAnimation()
        control_mid_video_status_img!!.visibility = View.VISIBLE
        control_mid_video_status_img!!.setImageDrawable(playStatusIcon)
        this.scaleType = scaleType
        if (this.scaleType == null) {
            cover!!.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            cover!!.scaleType = ImageView.ScaleType.FIT_CENTER
        }
        isFullScreenCover = fullScreen
        Glide.with(context)
                .load(videoInfo.cover)
                .into(cover)
    }


    fun resetCover() {
        initCover(videoInfo, scaleType, isFullScreenCover)
    }

    fun syncTouchProgress(touchProgress: Boolean) {
        isTouchProgress = touchProgress
    }

    fun initControlView() {
        if (coverControlLayer == null) return
        control_rb_duration!!.text = Utils.getVideoTimeStr(coverControlLayer!!.videoDuration)
        //单位精确到 0.1 秒
        control_b_progress!!.max = timeToProgress(coverControlLayer!!.videoDuration)

        if (processListener != null) {
            processListener!!.onDuration(coverControlLayer!!.videoDuration)
        }

        control_b_progress!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            private var videoPosition: Long = 0

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                videoPosition = progressToTime(progress)
                if (processListener != null) {
                    processListener!!.onCurrentPosition(videoPosition)
                }
                control_lb_position!!.text = Utils.getVideoTimeStr(videoPosition)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isTouchProgress = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                videoPosition = progressToTime(control_b_progress!!.progress)
                coverControlLayer!!.makeSurePlay()
                coverControlLayer!!.seekTo(videoPosition)
                if (processListener != null) {
                    processListener!!.onCurrentPosition(videoPosition)
                }
                control_lb_position!!.text = Utils.getVideoTimeStr(videoPosition)
                isTouchProgress = false
            }
        })
        postTrackerProgressRunnable()
    }

    fun setCoverImage(drawable: Drawable) {
        cover!!.setImageDrawable(drawable)
    }

    fun hideDisplayCover(hide: Boolean) {
        cover!!.visibility = if (hide) View.GONE else View.VISIBLE
    }

    fun setPlayImage(drawable: Drawable) {
        control_lb_play!!.setImageDrawable(drawable)
    }


    fun timeToProgress(videoTime: Long): Int {
        return (videoTime / 100).toInt()
    }

    private fun progressToTime(progress: Int): Long {
        return (progress * 100).toLong()
    }

    fun setProgress(progress: Int) {
        control_b_progress!!.progress = progress
    }

    fun notifyBufferProcess(bufferedPosition: Long) {
        if (processListener != null) {
            processListener!!.onBufferPosition(bufferedPosition)
        }
    }

    fun setBufferedProgress(bufferedProgress: Int) {
        control_b_progress!!.secondaryProgress = bufferedProgress
    }

    fun postHideControlRunnable() {
        removeCallbacks(innerHideControlRunnable)
        postDelayed(innerHideControlRunnable, CONTROL_AUTO_HIDDEN_MS)
    }

    fun removeControlRunnable() {
        removeCallbacks(innerHideControlRunnable)
    }

    fun postTrackerProgressRunnable() {
        removeCallbacks(innerTrackerProgressRunnable)
        post(innerTrackerProgressRunnable)
    }

    fun removeTrackerProgressRunnable() {
        removeCallbacks(innerTrackerProgressRunnable)
    }


    private inner class InnerHideControlRunnable : Runnable {

        override fun run() {
            isControlVisible = false
        }
    }

    inner class InnerTrackerProgressRunnable : Runnable {
        override fun run() {
            if (coverControlLayer != null) {
                val position = coverControlLayer!!.videoCurrentPosition
                val duration = coverControlLayer!!.videoDuration
                if (!isTouchProgress) {
                    setProgress(timeToProgress(position))
                }
                this@DefaultCoverView.postDelayed(this, QUERY_PROGRESS_MS)
            }
        }
    }

    companion object {
        private val QUERY_PROGRESS_MS: Long = 200
        private val CONTROL_AUTO_HIDDEN_MS = (5 * 1000).toLong()
    }
}
