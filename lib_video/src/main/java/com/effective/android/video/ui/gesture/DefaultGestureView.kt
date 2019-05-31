package com.effective.android.video.ui.gesture

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView

import com.effective.android.video.R
import com.effective.android.video.Utils
import com.effective.android.video.bean.GestureType
import com.effective.android.video.setting.AudioSetting
import com.effective.android.video.setting.BrightnessSetting


/**
 * 分离手势操作
 * created by yummylau 2019/04/20
 */
class DefaultGestureView : RelativeLayout, View.OnTouchListener {
    private var gesVolumeLayout: RelativeLayout? = null                 //手势：音量
    private var gesVolumeImg: ImageView? = null
    private var gesVolumePercentage: SeekBar? = null
    private var gesBrightLayout: RelativeLayout? = null                 //：亮度
    private var gesBrightPercentage: SeekBar? = null
    private var gesProgressLayout: LinearLayout? = null                 //：进度
    private var gesProgressTime: TextView? = null
    private var gesProgress: SeekBar? = null
    private var lessUnitModifyVolume: Int = 0
    private var modifyProgress: Long = 0
    private var currentProgress: Long = 0
    private var startModifyProgress: Boolean = false

    private var enableSingeTagUp = true
    private var enableDoubleTagUp = true
    private var enableModifyProgress = true
    private var enableModifyVolume = true
    private var enableModifyBright = true

    private val simpleOnGestureListener = VideoGestureListener()
    private val gestureDetector = GestureDetector(context, simpleOnGestureListener)
    private var gestureType = GestureType.ACTION_NONE
    private var gestureStatusListener: GestureStatusListener? = null
    private var gestureControlLayer: GestureControlLayer? = null


    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        val root = LayoutInflater.from(context).inflate(R.layout.video_default_gesture_view_layout, this, true)
        //音量
        gesVolumeLayout = root.findViewById(R.id.gesture_volume_layout)
        gesVolumeImg = root.findViewById(R.id.gesture_volume_img)
        gesVolumePercentage = root.findViewById(R.id.gesture_volume_progress)

        //亮度
        gesBrightLayout = root.findViewById(R.id.gesture_bright_layout)
        gesBrightPercentage = root.findViewById(R.id.gesture_bright_progress)

        //进度条
        gesProgressLayout = root.findViewById(R.id.gesture_progress_layout)
        gesProgressTime = root.findViewById(R.id.gesture_progress_time)
        gesProgress = root.findViewById(R.id.gesture_progress)
        setOnTouchListener(this)
        isLongClickable = true
    }

    fun setEnableSingeTagUp(enableSingeTagUp: Boolean) {
        this.enableSingeTagUp = enableSingeTagUp
    }

    fun setEnableDoubleTagUp(enableDoubleTagUp: Boolean) {
        this.enableDoubleTagUp = enableDoubleTagUp
    }

    fun setEnableModifyProgress(enableModifyProgress: Boolean) {
        this.enableModifyProgress = enableModifyProgress
    }

    fun setEnableModifyVolume(enableModifyVolume: Boolean) {
        this.enableModifyVolume = enableModifyVolume
    }

    fun setEnableModifyBright(enableModifyBright: Boolean) {
        this.enableModifyBright = enableModifyBright
    }

    /**
     * 必须设置
     *
     * @param gestureControlLayer
     */
    fun initPlayerGesture(gestureControlLayer: GestureControlLayer) {
        this.gestureControlLayer = gestureControlLayer
    }

    fun setGestureStatusListener(listener: GestureStatusListener) {
        this.gestureStatusListener = listener
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (gestureType == GestureType.MODIFY_PROGRESS && gestureControlLayer != null && enableModifyProgress) {
                    gestureControlLayer!!.handleModifyProgress(currentProgress)
                }
                if (gestureStatusListener != null && gestureType != GestureType.ACTION_NONE) {
                    gestureStatusListener!!.onDone()
                }
                gestureType = GestureType.ACTION_NONE
                modifyProgress = 0
                lessUnitModifyVolume = 0
                currentProgress = 0
                startModifyProgress = false
                if (gesVolumeLayout != null) gesVolumeLayout!!.visibility = View.GONE
                if (gesBrightLayout != null) gesBrightLayout!!.visibility = View.GONE
                if (gesProgressLayout != null) gesProgressLayout!!.visibility = View.GONE
            }
            MotionEvent.ACTION_CANCEL -> {
                if (gestureStatusListener != null && gestureType != GestureType.ACTION_NONE) {
                    gestureStatusListener!!.onDone()
                }
                gestureType = GestureType.ACTION_NONE
                modifyProgress = 0
                lessUnitModifyVolume = 0
                currentProgress = 0
                startModifyProgress = false
                if (gesVolumeLayout != null) gesVolumeLayout!!.visibility = View.GONE
                if (gesBrightLayout != null) gesBrightLayout!!.visibility = View.GONE
                if (gesProgressLayout != null) gesProgressLayout!!.visibility = View.GONE
            }
        }
        return gestureDetector.onTouchEvent(event)
    }

    /**
     * 监听手势事件
     */
    internal inner class VideoGestureListener : GestureDetector.SimpleOnGestureListener() {

        private var firstScroll = false

        override fun onDown(e: MotionEvent): Boolean {
            firstScroll = true
            return false
        }

        override fun onSingleTapConfirmed(event: MotionEvent): Boolean {  //单击
            return if (gestureControlLayer != null && enableSingeTagUp) {
                gestureControlLayer!!.handleSingleTapUp(event)
            } else false
        }

        override fun onDoubleTap(event: MotionEvent): Boolean {    //双击
            return if (gestureControlLayer != null && enableDoubleTagUp) {
                gestureControlLayer!!.handleDoubleTap(event)
            } else false
        }

        /**
         * 理解onScroll 在滑动过程中是离散调用的
         * 一次长滑动，多次distanceX 和 多次distanceY 相加会得到 最有一次e2和e1的对比结果
         * 规则：
         * 1. 横向的距离变化大则调整进度，纵向的变化大则调整音量亮度
         * 2. 音量和亮度的手势范围各占播放器一半
         *
         * @param e1        开始滑动坐标点
         * @param e2        结束滑动坐标点
         * @param distanceX 每次离散调用时的x滑动距离 distanceX>0 向左滑， distanceX<0 向右滑
         * @param distanceY 每次离散调用的的y滑动距离，distanceY>0 向上滑，distanceY<0 向下滑
         * @return
         */
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            val startX = e1.rawX
            val startY = e1.rawY
            val endX = e2.rawX
            val endY = e2.rawY
            Log.d(TAG, "startX($startX)  startY($startY)  endX( $endX) endY($endY) distanceX($distanceX) distanceY($distanceY)")
            if (gestureControlLayer == null || gestureControlLayer!!.isVideoPrepare()) {
                return false
            }
            val areaWidth = Utils.getWindowWidth(context)
            val areaHeight = Utils.getWindowHeight(context)

            if (firstScroll) {
                if (Math.abs(distanceX) >= Math.abs(distanceY)) {       //进度调整
                    if (enableModifyProgress) {
                        gestureType = GestureType.MODIFY_PROGRESS
                        showVideoProgressLayout()
                    } else {
                        return false
                    }

                } else {
                    if (startX > areaWidth * 1 / 2) {                  // 音量
                        if (enableModifyVolume) {
                            gestureType = GestureType.MODIFY_VOLUME
                            gesVolumeLayout!!.visibility = View.VISIBLE
                            gesBrightLayout!!.visibility = View.GONE
                            gesProgressLayout!!.visibility = View.GONE
                        } else {
                            return false
                        }
                    } else if (startX < areaWidth * 1 / 2) {           // 亮度
                        if (enableModifyBright) {
                            gestureType = GestureType.MODIFY_BRIGHT
                            gesVolumeLayout!!.visibility = View.GONE
                            gesBrightLayout!!.visibility = View.VISIBLE
                            gesProgressLayout!!.visibility = View.GONE
                        } else {
                            return false
                        }
                    }
                }
                if (gestureStatusListener != null) {
                    gestureStatusListener!!.onBegin()
                }
            }

            if (gestureControlLayer != null) {
                gestureControlLayer!!.onContinueEvent()
            }

            when (gestureType) {
                GestureType.MODIFY_PROGRESS -> {
                    if (Math.abs(distanceX) > Math.abs(distanceY)) {
                        val duration = gestureControlLayer!!.getDuration()
                        if (duration != 0L) {
                            val unitTimePx = duration * 1.0f / areaWidth                        //计算单位像素滑动视频多长
                            modifyProgress = (-distanceX * unitTimePx).toLong()

                            if (!startModifyProgress) {
                                currentProgress = gestureControlLayer!!.getPosition() + modifyProgress
                                startModifyProgress = true
                            } else {
                                currentProgress = currentProgress + modifyProgress
                            }


                            if (currentProgress > duration) {
                                currentProgress = duration
                            }

                            if (currentProgress < 0) {
                                currentProgress = 0
                            }
                            updateVideoProgress(currentProgress, duration)
                        }
                    }
                }

                GestureType.MODIFY_VOLUME -> {
                    if (Math.abs(distanceY) > Math.abs(distanceX)) {

                        val maxVolume = AudioSetting.getMaxVolume(context, AudioSetting.AudioType.MUSIC)
                        val currentVolume = AudioSetting.getCurrentVolume(context, AudioSetting.AudioType.MUSIC)

                        val largeMaxVolume = maxVolume * MODIFY_LARGE_UNIT
                        var largeCurrentVolume = currentVolume * MODIFY_LARGE_UNIT + lessUnitModifyVolume
                        val unitVolumePx = largeMaxVolume * 1.0f / areaHeight

                        largeCurrentVolume += (unitVolumePx * distanceY).toInt()

                        if (largeCurrentVolume > largeMaxVolume) {
                            largeCurrentVolume = largeMaxVolume
                        }
                        if (largeCurrentVolume < 0) {
                            largeCurrentVolume = 0
                        }

                        lessUnitModifyVolume = largeCurrentVolume % MODIFY_LARGE_UNIT
                        val realVolume = largeCurrentVolume / MODIFY_LARGE_UNIT
                        gesVolumePercentage!!.progress = largeCurrentVolume * 100 / largeMaxVolume
                        gesVolumeImg!!.setImageResource(if (gesVolumePercentage!!.progress == 0) R.drawable.video_ic_volum_zero else R.drawable.video_ic_volume)
                        AudioSetting.setCurrentVolume(context, AudioSetting.AudioType.MUSIC, realVolume, 0)
                    }
                }

                GestureType.MODIFY_BRIGHT -> {
                    if (Math.abs(distanceY) > Math.abs(distanceX)) {

                        //去除系统默认设置
                        val activity = Utils.scanForActivity(context)
                        var brightness = BrightnessSetting.getWindowBrightness(activity!!)
                        if (brightness < 0) {
                            brightness = BrightnessSetting.getScreenBrightness(activity) * 1.0f / 255
                        }

                        brightness = brightness + distanceY * 1.0f / areaHeight
                        if (brightness > 1.0f) {
                            brightness = 1.0f
                        } else if (brightness < 0.01f) {
                            brightness = 0.00f
                        }
                        Log.d(TAG, "volume: distanceY($distanceY)  brightness($brightness)")
                        gesBrightPercentage!!.progress = (brightness * 100).toInt()
                        BrightnessSetting.setWindowBrightness(activity, brightness)
                    }
                }
                else -> {
                }
            }
            firstScroll = false        // 第一次scroll执行完成，修改标志
            return false
        }
    }

    fun showSeekProgressHint(isDone: Boolean, currentProgressInMs: Long) {
        if (gestureControlLayer != null) {
            val durationMs = gestureControlLayer!!.getDuration()
            if (isDone) {
                gesProgressLayout!!.visibility = View.GONE
            } else {
                updateVideoProgress(currentProgressInMs, durationMs)
            }
        }
    }

    fun updateVideoProgress(currentProgressInMs: Long, durationInMs: Long) {
        if (gesProgressLayout!!.visibility != View.VISIBLE) {
            showVideoProgressLayout()
        }
        gesProgressTime!!.text = Utils.getVideoTimeStr(currentProgressInMs) + " / " + Utils.getVideoTimeStr(durationInMs)
        gesProgress!!.progress = (currentProgressInMs * 100 / durationInMs).toInt()
    }

    fun showVideoProgressLayout() {
        gesVolumeLayout!!.visibility = View.GONE
        gesBrightLayout!!.visibility = View.GONE
        gesProgressLayout!!.visibility = View.VISIBLE
    }

    companion object {

        private val TAG = DefaultGestureView::class.java.simpleName

        //手势相关逻辑
        private val MODIFY_LARGE_UNIT = 1000
    }
}
