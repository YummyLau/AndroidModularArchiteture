package com.effective.android.video.ui.cover

import android.content.Context
import android.graphics.drawable.Drawable
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

/**
 * 默认提供的浮层view
 * Created by yummylau on 2019/04/20.
 */
class DefaultCoverView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var controlView: RelativeLayout? = null
    private var lbPlay: ImageView? = null                               //底部控制栏：左下播放按钮
    private var lbPosition: TextView? = null                            //底部控制栏：左下播放时间
    private var rbDuration: TextView? = null                            //底部控制栏：视频总时长
    private var rbFullScreen: ImageView? = null                         //底部控制栏：右下全屏按钮
    private var bProgress: SeekBar? = null                              //底部控制栏：底部进度条

    private var ltBack: ImageView? = null
    private var rtVolume: ImageView? = null                     //顶部栏：返回，音量

    private var midStatusImg: ImageView? = null                         //中间：播放暂停按钮，loading，重播，中部提示
    private var midStatusTip: TextView? = null

    private var coverLayout: View? = null                               //封面：封面图片，时间提示
    private var cover: ImageView? = null
    private var videoTimeTip: TextView? = null                          //右下角视频事件提示（悬浮）

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

    val isMidStatusImgVisible: Boolean
        get() = midStatusImg!!.visibility == View.VISIBLE

    //    public void clearGlide() {
    //        GLImageLoader.clearRequest(cover);
    //    }
    //
    //    public void clearCover() {
    //        if (cover != null) {
    //            cover.setImageDrawable(null);
    //        }
    //    }

    var isControlVisible: Boolean
        get() = controlView!!.visibility == View.VISIBLE
        set(visible) {
            controlView!!.visibility = if (visible) View.VISIBLE else View.GONE
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
                controlListener!!.onControlVisible(controlView!!.visibility == View.VISIBLE)
            }
        }

    private val loadingAnimation: Animation?
        get() {
            if (loadingAni == null) {
                loadingAni = AnimationUtils.loadAnimation(context, R.anim.video_loading_anim)
            }
            return loadingAni
        }

    private var scaleType: ImageView.ScaleType? = null
    private var isFullScreenCover: Boolean = false
    private var videoInfo: VideoInfo? = null

    init {
        initView(context, attrs, defStyleAttr)
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        //顶部
        val root = LayoutInflater.from(context).inflate(R.layout.video_default_cover_view_layout, this, true)
        ltBack = root.findViewById(R.id.control_lt_back)
        rtVolume = root.findViewById(R.id.control_rt_volume)

        //中部
        midStatusImg = root.findViewById(R.id.control_mid_video_status_img)
        midStatusTip = root.findViewById(R.id.control_mid_video_status_tip)

        //底部控制栏
        controlView = root.findViewById(R.id.control_bottom_layout)
        rbFullScreen = root.findViewById(R.id.control_rb_full_screen)
        lbPlay = root.findViewById(R.id.control_lb_play)
        lbPosition = root.findViewById(R.id.control_lb_position)
        rbDuration = root.findViewById(R.id.control_rb_duration)
        bProgress = root.findViewById(R.id.control_b_progress)

        //封面
        coverLayout = root.findViewById(R.id.cover_layout)
        cover = root.findViewById(R.id.cover)
        videoTimeTip = root.findViewById(R.id.control_rb_video_time)

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
        playIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_video_play)
        pauseIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_video_pause)

        if (playStatusIcon == null) {
            playStatusIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_video_play_state)
        }
        if (pauseStatusIcon == null) {
            pauseStatusIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_video_pause_state)
        }
        if (replayIcon == null) {
            replayIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_video_replay_state)
        }
        if (loadingStatusIcon == null) {
            loadingStatusIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_video_loading_state)
        }
        if (backIcon == null) {
            backIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_top_left_back)
        }
        if (volumeIcon == null) {
            volumeIcon = ContextCompat.getDrawable(getContext(), R.drawable.video_ic_top_right_volume)
        }

        midStatusImg!!.setImageDrawable(loadingStatusIcon)
        ltBack!!.setImageDrawable(backIcon)
        rtVolume!!.setImageDrawable(volumeIcon)

        rtVolume!!.visibility = if (enableVolume) View.VISIBLE else View.GONE
        ltBack!!.visibility = if (enableBack) View.VISIBLE else View.GONE

        coverLayout!!.visibility = View.GONE

        ltBack!!.setOnClickListener(this)
        rtVolume!!.setOnClickListener(this)
        rbFullScreen!!.setOnClickListener(this)
        lbPlay!!.setOnClickListener(this)
        midStatusTip!!.setOnClickListener(this)
        midStatusImg!!.setOnClickListener(this)

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
        midStatusImg!!.setImageDrawable(loadingStatusIcon)
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
                if (coverControlLayer != null && coverControlLayer!!.isFullScreen) {
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
                rbFullScreen!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.video_ic_collapse))
            }
        }
    }

    fun exitFullScreen() {
        if (controlListener != null && controlListener!!.onFullScreen(false)) {
            if (coverControlLayer != null) {
                coverControlLayer!!.exitFullScreen()
                rbFullScreen!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.video_ic_unfold))
            }
        }
    }

    private fun coverVisible(): Boolean {
        return coverLayout!!.visibility == View.VISIBLE
    }

    fun setCoverVisible(visible: Boolean) {
        coverLayout!!.visibility = if (visible) View.VISIBLE else View.GONE
        if (visible) {
            if (coverControlLayer != null && coverControlLayer!!.isVideoLoaded) {
                setVideoTimeTip(coverControlLayer!!.videoDuration, 0)
            } else if (coverControlLayer != null && coverControlLayer!!.isMediaInfoLoaded) {
                setVideoTimeTip(coverControlLayer!!.mediaDuration, 0)
            } else if (videoInfo != null) {
                setVideoTimeTip(videoInfo!!.duration, 0)
            } else {
                videoTimeTip!!.visibility = View.GONE
            }
        }
    }

    fun setCoverColor(@DrawableRes resId: Int) {
        if (cover != null) {
            cover!!.setImageResource(resId)
        }
    }

    fun setVideoTimeTip(duration: Long, position: Long) {
        if (enableVideoTime) {
            if (duration > position) {
                videoTimeTip!!.visibility = View.VISIBLE
                videoTimeTip!!.text = Utils.getRemainingVideoTimeStr(duration, position)
            } else {
                videoTimeTip!!.visibility = View.GONE
            }
        } else {
            videoTimeTip!!.visibility = View.GONE
        }
    }

    private fun setMidStatusImg(videoStatus: VideoStatus, isVisible: Boolean) {
        when (videoStatus) {
            VideoStatus.PREPARE -> {
                if (isVisible) {
                    midStatusImg!!.clearAnimation()
                    midStatusImg!!.visibility = View.VISIBLE
                    midStatusImg!!.setImageDrawable(loadingStatusIcon)
                    midStatusImg!!.startAnimation(loadingAnimation)
                } else {
                    midStatusImg!!.clearAnimation()
                    midStatusImg!!.visibility = View.GONE
                }
            }
            VideoStatus.BUFFERING -> {
                if (isVisible) {
                    midStatusImg!!.clearAnimation()
                    midStatusImg!!.visibility = View.VISIBLE
                    midStatusImg!!.setImageDrawable(loadingStatusIcon)
                    midStatusImg!!.startAnimation(loadingAnimation)
                } else {
                    midStatusImg!!.clearAnimation()
                    midStatusImg!!.visibility = View.GONE
                }
            }

            VideoStatus.PLAYING -> {
                midStatusImg!!.clearAnimation()
                midStatusImg!!.setImageDrawable(pauseStatusIcon)
                lbPlay!!.setImageDrawable(pauseIcon)
                midStatusImg!!.visibility = if (isVisible) View.VISIBLE else View.GONE
            }

            VideoStatus.PAUSE -> {
                midStatusImg!!.clearAnimation()
                midStatusImg!!.setImageDrawable(playStatusIcon)
                lbPlay!!.setImageDrawable(playIcon)
                midStatusImg!!.visibility = if (isVisible) View.VISIBLE else View.GONE
            }

            else -> {
                midStatusImg!!.clearAnimation()
                midStatusImg!!.visibility = View.GONE
            }
        }
    }

    fun onVideoStatus(status: VideoStatus, msg: String) {
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
            //                midStatusTip.setText(R.string.video_load_error);
        }
        if (statusListener != null) {
            statusListener!!.onStatus(status, msg)
        }
        videoStatus = status
    }


    fun setMidStatusTip(visible: Boolean) {
        midStatusTip!!.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun hideControlView() {
        isControlVisible = false
    }

    fun initCover(videoInfo: VideoInfo?, scaleType: ImageView.ScaleType?, fullScreen: Boolean) {
        if (videoInfo == null) {
            setCoverVisible(false)
            midStatusImg!!.visibility = View.GONE
            return
        }
        this.videoInfo = videoInfo
        setCoverVisible(true)
        midStatusImg!!.clearAnimation()
        midStatusImg!!.visibility = View.VISIBLE
        midStatusImg!!.setImageDrawable(playStatusIcon)
        this.scaleType = scaleType
        if (this.scaleType == null) {
            cover!!.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            cover!!.scaleType = ImageView.ScaleType.FIT_CENTER
        }
        isFullScreenCover = fullScreen
        //        GLImageLoader.disMediaCover(cover, videoInfo.cover, isFullScreenCover);
    }

    fun resetCover() {
        initCover(videoInfo, scaleType, isFullScreenCover)
    }

    fun syncTouchProgress(touchProgress: Boolean) {
        isTouchProgress = touchProgress
    }

    fun initControlView() {
        if (coverControlLayer == null) return
        rbDuration!!.text = Utils.getVideoTimeStr(coverControlLayer!!.videoDuration)
        //单位精确到 0.1 秒
        bProgress!!.max = timeToProgress(coverControlLayer!!.videoDuration)

        if (processListener != null) {
            processListener!!.onDuration(coverControlLayer!!.videoDuration)
        }

        bProgress!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            private var videoPosition: Long = 0

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                videoPosition = progressToTime(progress)
                if (processListener != null) {
                    processListener!!.onCurrentPosition(videoPosition)
                }
                lbPosition!!.text = Utils.getVideoTimeStr(videoPosition)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isTouchProgress = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                videoPosition = progressToTime(bProgress!!.progress)
                coverControlLayer!!.makeSurePlay()
                coverControlLayer!!.seekTo(videoPosition)
                if (processListener != null) {
                    processListener!!.onCurrentPosition(videoPosition)
                }
                lbPosition!!.text = Utils.getVideoTimeStr(videoPosition)
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
        lbPlay!!.setImageDrawable(drawable)
    }


    fun timeToProgress(videoTime: Long): Int {
        return (videoTime / 100).toInt()
    }

    private fun progressToTime(progress: Int): Long {
        return (progress * 100).toLong()
    }

    fun setProgress(progress: Int) {
        bProgress!!.progress = progress
    }

    fun notifyBufferProcess(bufferedPosition: Long) {
        if (processListener != null) {
            processListener!!.onBufferPosition(bufferedPosition)
        }
    }

    fun setBufferedProgress(bufferedProgress: Int) {
        bProgress!!.secondaryProgress = bufferedProgress
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
